package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorRiesgoRestanteTest {
    @Test
    fun `escena totalmente corregida no tiene objetos sin corregir`() {
        val escena = Escena("cuarto", listOf(ObjetoRiesgo("cable", "Cable", corregido = true)))
        assertTrue(MotorRiesgoRestante.objetosSinCorregir(escena).isEmpty())
    }

    @Test
    fun `objetosSinCorregir devuelve solo los objetos con corregido en false`() {
        val escena = Escena(
            "cuarto",
            listOf(
                ObjetoRiesgo("cable", "Cable", corregido = true),
                ObjetoRiesgo("mueble", "Mueble", corregido = false),
            ),
        )
        val restantes = MotorRiesgoRestante.objetosSinCorregir(escena)
        assertEquals(1, restantes.size)
        assertEquals("mueble", restantes.first().id)
    }

    @Test
    fun `cantidadRiesgosRestantes cuenta cuantos objetos faltan por corregir`() {
        val escena = Escena(
            "cocina",
            listOf(
                ObjetoRiesgo("olla", "Olla", corregido = false),
                ObjetoRiesgo("liquido", "Líquido", corregido = false),
                ObjetoRiesgo("cable", "Cable", corregido = true),
            ),
        )
        assertEquals(2, MotorRiesgoRestante.cantidadRiesgosRestantes(escena))
    }

    @Test
    fun `cantidadRiesgosRestantes es cero cuando todo esta corregido`() {
        val escena = Escena("cuarto", listOf(ObjetoRiesgo("cable", "Cable", corregido = true)))
        assertEquals(0, MotorRiesgoRestante.cantidadRiesgosRestantes(escena))
    }

    @Test
    fun `una escena sin objetos tiene cero riesgos restantes`() {
        val escena = Escena("vacia", emptyList())
        assertEquals(0, MotorRiesgoRestante.cantidadRiesgosRestantes(escena))
    }

    // Sección 5.12 del maestro: un distractor sin tocar no es un riesgo pendiente.
    @Test
    fun `un distractor sin tocar no cuenta como riesgo restante`() {
        val escena = Escena(
            "cuarto",
            listOf(
                ObjetoRiesgo("cable", "Cable", corregido = true, esRiesgo = true),
                ObjetoRiesgo("cable_guardado", "Cable guardado", corregido = false, esRiesgo = false),
            ),
        )
        assertEquals(0, MotorRiesgoRestante.cantidadRiesgosRestantes(escena))
        assertTrue(MotorRiesgoRestante.objetosSinCorregir(escena).isEmpty())
    }
}
