package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo

object MotorRiesgoRestante {
    // Un distractor no es un riesgo pendiente: no cuenta, se haya tocado o no (sección 5.12).
    fun objetosSinCorregir(escena: Escena): List<ObjetoRiesgo> =
        escena.objetos.filter { it.esRiesgo && !it.corregido }

    fun cantidadRiesgosRestantes(escena: Escena): Int = objetosSinCorregir(escena).size
}
