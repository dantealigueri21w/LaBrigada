package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.CorreccionRegistrada
import pe.appmobile.labrigada.domain.model.EstadoLugar
import java.util.concurrent.TimeUnit

object MotorProgreso {
    fun calcularNuevasInsignias(
        historial: List<CorreccionRegistrada>,
        insigniasYaGanadas: Set<String>,
    ): Set<String> {
        val nuevas = mutableSetOf<String>()

        if (historial.isNotEmpty() && "primera_correccion" !in insigniasYaGanadas) {
            nuevas += "primera_correccion"
        }

        val lugaresDistintos = historial.map { it.lugarId }.toSet()
        if (lugaresDistintos.size >= 8 && "brigada_completa" !in insigniasYaGanadas) {
            nuevas += "brigada_completa"
        }

        if (historial.size >= 20 && "bitacora_llena" !in insigniasYaGanadas) {
            nuevas += "bitacora_llena"
        }

        return nuevas
    }

    fun calcularRacha(historial: List<CorreccionRegistrada>, hoy: Long): Int {
        if (historial.isEmpty()) return 0
        val diasConActividad = historial.map { TimeUnit.MILLISECONDS.toDays(it.fecha) }.toSortedSet()
        var racha = 0
        var diaActual = TimeUnit.MILLISECONDS.toDays(hoy)
        while (diaActual in diasConActividad) {
            racha++
            diaActual--
        }
        return racha
    }

    /**
     * Los 3 primeros lugares del orden semilla arrancan abiertos sin condición (sección 5.1 v13
     * del maestro); cada lugar completado abre el siguiente del orden, así que completar 2 de
     * los 3 iniciales ya deja 5 disponibles en la primera sesión.
     */
    fun estaDesbloqueado(orden: Int, lugaresCompletados: Int): Boolean =
        orden <= 3 || lugaresCompletados >= orden - 3

    fun calcularEstadoLugar(desbloqueado: Boolean, completado: Boolean, tuvoIntentoFallido: Boolean): EstadoLugar = when {
        !desbloqueado -> EstadoLugar.BLOQUEADO
        completado && !tuvoIntentoFallido -> EstadoLugar.DOMINADO
        completado -> EstadoLugar.COMPLETADO
        tuvoIntentoFallido -> EstadoLugar.INICIADO
        else -> EstadoLugar.DISPONIBLE
    }
}
