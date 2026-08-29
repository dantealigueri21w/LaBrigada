package pe.appmobile.labrigada.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.data.repository.BrigadaRepository
import pe.appmobile.labrigada.domain.engine.MotorEscena
import pe.appmobile.labrigada.domain.engine.MotorRiesgoRestante
import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo
import pe.appmobile.labrigada.domain.model.ReglaDistancia
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro

/** Ids que se resuelven tocando, no arrastrando -- no representan un objeto que se pueda cargar. */
private val OBJETOS_QUE_SE_TOCAN = setOf("cruzar_sin_mirar", "semaforo_ignorado")

data class LugarUiState(
    val lugar: LugarEntity? = null,
    val objetos: List<ObjetoRiesgoEntity> = emptyList(),
    val corregidos: Set<String> = emptySet(),
    val riesgosRestantes: Int = 0,
    val escenaSegura: Boolean = false,
    /**
     * Id del último objeto que el niño acaba de corregir, para poder decírselo en pantalla. Se
     * guarda el id y no el nombre porque la pantalla también necesita saber si era una conducta
     * o un objeto: el mensaje de confirmación no es el mismo para los dos.
     */
    val ultimoCorregidoId: String? = null,
    val resultadoSimulacro: ResultadoSimulacro? = null,
    val cargando: Boolean = true,
) {
    fun objetoSeTocaNoSeArrastra(objetoId: String) = objetoId in OBJETOS_QUE_SE_TOCAN
}

class LugarViewModel(
    private val repository: BrigadaRepository,
    private val lugarId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LugarUiState())
    val uiState: StateFlow<LugarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sembrarSiEsPrimerLanzamiento()
            val lugar = repository.obtenerLugares().first { it.id == lugarId }
            // Ningun objeto arranca ya corregido (seccion 5.7 del maestro): "corregidos" siempre
            // empieza vacio, sin importar cuantas veces se haya jugado este lugar antes.
            val objetos = repository.obtenerObjetosDeLugar(lugarId)
            _uiState.value = _uiState.value.copy(
                lugar = lugar,
                objetos = objetos,
                // Solo los objetos de riesgo cuentan (sección 5.12): un distractor nunca fue
                // parte de "lo que falta por corregir".
                riesgosRestantes = objetos.count { it.esRiesgo },
                cargando = false,
            )
        }
    }

    /**
     * Se llama solo cuando el niño de verdad resolvió a este objeto: al soltarlo dentro de SU
     * PROPIA zona (una por objeto, sección 1.2 del archivo de lecciones: soltar en la zona de
     * otro objeto no dispara nada, porque cada [pe.appmobile.labrigada.ui.components.FichaArrastrable]
     * solo compara contra su propia zona de destino) o al tocarlo si es de conducta.
     */
    fun corregirObjeto(objetoId: String) {
        val estado = _uiState.value
        if (estado.escenaSegura || objetoId in estado.corregidos) return
        val objeto = estado.objetos.firstOrNull { it.id == objetoId } ?: return

        if (!objeto.esRiesgo) {
            // Distractor (sección 5.12 del maestro): ya estaba bien. Nunca entra a
            // "corregidos", nunca mueve el contador ni la condición de victoria -- solo deja
            // la señal de que se tocó, para que Firu explique por qué no hacía falta.
            _uiState.value = estado.copy(ultimoCorregidoId = objetoId)
            return
        }

        val nuevosCorregidos = estado.corregidos + objetoId
        val escenaTentativa = construirEscena(estado.objetos, nuevosCorregidos)
        val segura = MotorEscena.esEscenaSegura(escenaTentativa)
        val restantes = MotorRiesgoRestante.cantidadRiesgosRestantes(escenaTentativa)

        _uiState.value = estado.copy(
            corregidos = nuevosCorregidos,
            riesgosRestantes = restantes,
            escenaSegura = segura,
            // Corregir no daba ninguna señal en palabras: el objeto se atenuaba y el contador
            // bajaba, pero nadie decía QUÉ acababa de pasar ni con cuál objeto.
            ultimoCorregidoId = objetoId,
        )

        if (segura) {
            viewModelScope.launch {
                if (lugarId == "simulacro_final") {
                    _uiState.value = _uiState.value.copy(resultadoSimulacro = repository.evaluarSimulacroFinal(escenaTentativa))
                } else {
                    repository.registrarCorreccion(lugarId, escenaTentativa)
                }
            }
        }
    }

    /**
     * [distanciaMinimaCumplida] se deriva de si el objeto dependiente ya está corregido, no de
     * una medición geométrica en vivo -- simplificación documentada (ver "Antes de empezar" del
     * plan de la Parte 3 y arte/60-...md sección 3): cada zona segura ya se diseña a una
     * distancia real del ancla, así que "corregido" ya implica "a distancia segura".
     */
    private fun construirEscena(objetos: List<ObjetoRiesgoEntity>, corregidos: Set<String>): Escena {
        val objetosDominio = objetos.map {
            ObjetoRiesgo(id = it.id, nombre = it.nombre, corregido = it.id in corregidos, esRiesgo = it.esRiesgo)
        }
        val reglas = objetos.filter { it.distanciaMinimaDeId != null }.map { objeto ->
            ReglaDistancia(
                objetoAId = objeto.id,
                objetoBId = objeto.distanciaMinimaDeId!!,
                distanciaMinimaCumplida = objeto.id in corregidos,
            )
        }
        return Escena(id = lugarId, objetos = objetosDominio, reglasDistancia = reglas)
    }

    class Factory(
        private val repository: BrigadaRepository,
        private val lugarId: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = LugarViewModel(repository, lugarId) as T
    }
}
