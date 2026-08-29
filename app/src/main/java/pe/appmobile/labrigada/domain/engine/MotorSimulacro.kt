package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro

object MotorSimulacro {
    fun evaluarSimulacro(escena: Escena): ResultadoSimulacro {
        // Un distractor sin tocar no es un fallo: nunca hizo falta corregirlo (sección 5.12).
        val fallaron = escena.objetos.filter { it.esRiesgo && !it.corregido }.map { it.id }
        val reglasOk = escena.reglasDistancia.all { it.distanciaMinimaCumplida }
        return ResultadoSimulacro(
            lugarId = escena.id,
            paso = fallaron.isEmpty() && reglasOk,
            objetosQueFallaron = fallaron,
        )
    }
}
