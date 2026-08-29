package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo
import pe.appmobile.labrigada.domain.model.ReglaDistancia
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorEscenaTest {
    @Test
    fun `escena con todos los objetos corregidos y sin reglas de distancia es segura`() {
        val escena = Escena(
            id = "cuarto",
            objetos = listOf(ObjetoRiesgo("cable", "Cable suelto", corregido = true)),
        )
        assertTrue(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `escena con un objeto sin corregir no es segura`() {
        val escena = Escena(
            id = "cuarto",
            objetos = listOf(
                ObjetoRiesgo("cable", "Cable suelto", corregido = true),
                ObjetoRiesgo("mueble", "Mueble cerca de la cama", corregido = false),
            ),
        )
        assertFalse(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `escena con objetos corregidos pero una regla de distancia sin cumplir no es segura`() {
        val escena = Escena(
            id = "campamento",
            objetos = listOf(
                ObjetoRiesgo("fogata", "Fogata", corregido = true),
                ObjetoRiesgo("carpa", "Carpa", corregido = true),
            ),
            reglasDistancia = listOf(ReglaDistancia("fogata", "carpa", distanciaMinimaCumplida = false)),
        )
        assertFalse(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `escena con objetos corregidos y regla de distancia cumplida es segura`() {
        val escena = Escena(
            id = "campamento",
            objetos = listOf(
                ObjetoRiesgo("fogata", "Fogata", corregido = true),
                ObjetoRiesgo("carpa", "Carpa", corregido = true),
            ),
            reglasDistancia = listOf(ReglaDistancia("fogata", "carpa", distanciaMinimaCumplida = true)),
        )
        assertTrue(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `una escena sin objetos ni reglas se considera segura por vacuidad`() {
        val escena = Escena(id = "vacia", objetos = emptyList())
        assertTrue(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `con varias reglas de distancia todas deben cumplirse`() {
        val escena = Escena(
            id = "cocina",
            objetos = listOf(ObjetoRiesgo("olla", "Olla", corregido = true)),
            reglasDistancia = listOf(
                ReglaDistancia("olla", "tomacorriente", distanciaMinimaCumplida = true),
                ReglaDistancia("liquido", "tomacorriente", distanciaMinimaCumplida = false),
            ),
        )
        assertFalse(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `objetos corregidos con multiples reglas todas cumplidas da escena segura`() {
        val escena = Escena(
            id = "cocina",
            objetos = listOf(ObjetoRiesgo("olla", "Olla", corregido = true)),
            reglasDistancia = listOf(
                ReglaDistancia("olla", "tomacorriente", distanciaMinimaCumplida = true),
                ReglaDistancia("liquido", "tomacorriente", distanciaMinimaCumplida = true),
            ),
        )
        assertTrue(MotorEscena.esEscenaSegura(escena))
    }

    // Sección 5.12 del maestro: un distractor (esRiesgo = false) nunca puede ser lo que decide
    // si la escena queda segura. "Marcar todos los elementos" no puede ser la condición de
    // victoria -- solo importa si los objetos de riesgo real están corregidos.

    @Test
    fun `un distractor sin tocar no impide que la escena quede segura`() {
        val escena = Escena(
            id = "cuarto",
            objetos = listOf(
                ObjetoRiesgo("cable", "Cable suelto", corregido = true, esRiesgo = true),
                ObjetoRiesgo("cable_guardado", "Cable enrollado y guardado", corregido = false, esRiesgo = false),
            ),
        )
        assertTrue(MotorEscena.esEscenaSegura(escena))
    }

    @Test
    fun `tocar el distractor no compensa un riesgo real sin corregir`() {
        val escena = Escena(
            id = "cuarto",
            objetos = listOf(
                ObjetoRiesgo("cable", "Cable suelto", corregido = false, esRiesgo = true),
                ObjetoRiesgo("cable_guardado", "Cable enrollado y guardado", corregido = true, esRiesgo = false),
            ),
        )
        assertFalse(MotorEscena.esEscenaSegura(escena))
    }
}
