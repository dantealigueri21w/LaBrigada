package pe.appmobile.labrigada.domain.engine

import pe.appmobile.labrigada.domain.model.CorreccionRegistrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorProgresoTest {
    private fun correccion(lugar: String, fecha: Long = 0L) =
        CorreccionRegistrada(lugarId = lugar, fecha = fecha, escenaQuedoSegura = true)

    @Test
    fun `sin correcciones no hay insignias nuevas`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(emptyList(), emptySet())
        assertTrue(nuevas.isEmpty())
    }

    @Test
    fun `la primera correccion otorga Primera Correccion`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(listOf(correccion("cuarto")), emptySet())
        assertTrue("primera_correccion" in nuevas)
    }

    @Test
    fun `Primera Correccion no se repite si ya estaba ganada`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(listOf(correccion("cuarto")), setOf("primera_correccion"))
        assertFalse("primera_correccion" in nuevas)
    }

    @Test
    fun `corregir los 8 lugares distintos otorga Brigada Completa`() {
        val historial = (1..8).map { correccion("lugar$it") }
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, setOf("primera_correccion"))
        assertTrue("brigada_completa" in nuevas)
    }

    @Test
    fun `7 lugares distintos no otorgan Brigada Completa todavia`() {
        val historial = (1..7).map { correccion("lugar$it") }
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, setOf("primera_correccion"))
        assertFalse("brigada_completa" in nuevas)
    }

    @Test
    fun `calcularRacha cuenta dias consecutivos con al menos una correccion`() {
        val unDia = 24L * 60 * 60 * 1000
        val historial = listOf(correccion("l1", 0L), correccion("l2", unDia), correccion("l3", unDia * 2))
        assertEquals(3, MotorProgreso.calcularRacha(historial, hoy = unDia * 2))
    }

    @Test
    fun `sin progreso hay exactamente 3 lugares desbloqueados`() {
        val abiertos = (1..8).count { orden -> MotorProgreso.estaDesbloqueado(orden, lugaresCompletados = 0) }
        assertEquals(3, abiertos)
    }

    @Test
    fun `con 2 lugares completados hay 5 desbloqueados`() {
        val abiertos = (1..8).count { orden -> MotorProgreso.estaDesbloqueado(orden, lugaresCompletados = 2) }
        assertEquals(5, abiertos)
    }

    @Test
    fun `el octavo lugar se abre con 5 completados`() {
        assertTrue(MotorProgreso.estaDesbloqueado(orden = 8, lugaresCompletados = 5))
        assertFalse(MotorProgreso.estaDesbloqueado(orden = 8, lugaresCompletados = 4))
    }
}
