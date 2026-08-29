package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena

object MotorEscena {
    // Solo los objetos de riesgo cuentan para que la escena quede segura (sección 5.12 del
    // maestro): un distractor nunca está "sin corregir" porque nunca necesitó corregirse.
    fun esEscenaSegura(escena: Escena): Boolean =
        escena.objetos.filter { it.esRiesgo }.all { it.corregido } &&
            escena.reglasDistancia.all { it.distanciaMinimaCumplida }
}
