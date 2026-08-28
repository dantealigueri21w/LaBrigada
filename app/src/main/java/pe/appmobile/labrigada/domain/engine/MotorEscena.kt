package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena

object MotorEscena {
    fun esEscenaSegura(escena: Escena): Boolean =
        escena.objetos.all { it.corregido } && escena.reglasDistancia.all { it.distanciaMinimaCumplida }
}
