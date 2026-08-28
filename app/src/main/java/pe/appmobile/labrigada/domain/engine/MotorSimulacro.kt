package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ResultadoSimulacro

object MotorSimulacro {
    fun evaluarSimulacro(escena: Escena): ResultadoSimulacro {
        val fallaron = escena.objetos.filter { !it.corregido }.map { it.id }
        val reglasOk = escena.reglasDistancia.all { it.distanciaMinimaCumplida }
        return ResultadoSimulacro(
            lugarId = escena.id,
            paso = fallaron.isEmpty() && reglasOk,
            objetosQueFallaron = fallaron,
        )
    }
}
