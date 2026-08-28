package pe.appmobile.labrigada.domain.model

data class ObjetoRiesgo(
    val id: String,
    val nombre: String,
    val corregido: Boolean,
)

/**
 * Algunas reglas de seguridad son relativas entre dos objetos (ej. "la vela lejos del
 * abrigo"), no solo "este objeto está en su lugar correcto". [distanciaMinimaCumplida] lo
 * modela sin necesitar coordenadas reales de pantalla en el dominio.
 */
data class ReglaDistancia(
    val objetoAId: String,
    val objetoBId: String,
    val distanciaMinimaCumplida: Boolean,
)

data class Escena(
    val id: String,
    val objetos: List<ObjetoRiesgo>,
    val reglasDistancia: List<ReglaDistancia> = emptyList(),
)

data class ResultadoSimulacro(
    val lugarId: String,
    val paso: Boolean,
    val objetosQueFallaron: List<String>,
)

data class CorreccionRegistrada(
    val lugarId: String,
    val fecha: Long,
    val escenaQuedoSegura: Boolean,
)
