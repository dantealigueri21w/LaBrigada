package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo

object MotorRiesgoRestante {
    fun objetosSinCorregir(escena: Escena): List<ObjetoRiesgo> = escena.objetos.filter { !it.corregido }

    fun cantidadRiesgosRestantes(escena: Escena): Int = objetosSinCorregir(escena).size
}
