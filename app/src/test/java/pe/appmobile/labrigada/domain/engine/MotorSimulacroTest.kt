package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo
import pe.appmobile.labrigada.domain.model.ReglaDistancia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorSimulacroTest {
    @Test
    fun `simulacro con escena totalmente segura pasa`() {
        val escena = Escena("final", listOf(ObjetoRiesgo("a", "A", corregido = true)))
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertTrue(resultado.paso)
    }

    @Test
    fun `simulacro con un objeto sin corregir no pasa`() {
        val escena = Escena(
            "final",
            listOf(ObjetoRiesgo("a", "A", corregido = true), ObjetoRiesgo("b", "B", corregido = false)),
        )
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertFalse(resultado.paso)
    }

    @Test
    fun `simulacro reporta los ids exactos de los objetos que fallaron`() {
        val escena = Escena(
            "final",
            listOf(ObjetoRiesgo("a", "A", corregido = true), ObjetoRiesgo("b", "B", corregido = false)),
        )
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertEquals(listOf("b"), resultado.objetosQueFallaron)
    }

    @Test
    fun `simulacro con regla de distancia incumplida no pasa aunque los objetos esten corregidos`() {
        val escena = Escena(
            "final",
            objetos = listOf(ObjetoRiesgo("fogata", "Fogata", corregido = true)),
            reglasDistancia = listOf(ReglaDistancia("fogata", "carpa", distanciaMinimaCumplida = false)),
        )
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertFalse(resultado.paso)
    }

    @Test
    fun `simulacro que pasa no reporta ningun objeto que fallo`() {
        val escena = Escena("final", listOf(ObjetoRiesgo("a", "A", corregido = true)))
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertTrue(resultado.objetosQueFallaron.isEmpty())
    }

    // Sección 5.12 del maestro: un distractor sin tocar nunca es un fallo del simulacro.
    @Test
    fun `un distractor sin tocar no hace fallar el simulacro ni aparece entre los que fallaron`() {
        val escena = Escena(
            "final",
            listOf(
                ObjetoRiesgo("a", "A", corregido = true, esRiesgo = true),
                ObjetoRiesgo("b_seguro", "B ya seguro", corregido = false, esRiesgo = false),
            ),
        )
        val resultado = MotorSimulacro.evaluarSimulacro(escena)
        assertTrue(resultado.paso)
        assertTrue(resultado.objetosQueFallaron.isEmpty())
    }
}
