package pe.appmobile.labrigada.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.AppDatabase
import pe.appmobile.labrigada.domain.model.Escena
import pe.appmobile.labrigada.domain.model.ObjetoRiesgo
import pe.appmobile.labrigada.domain.model.ReglaDistancia

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BrigadaRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: BrigadaRepository

    @Before
    fun crearDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        repository = BrigadaRepository(db)
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `sembrar en una base de datos vacia inserta los 8 lugares y sus objetos reales`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        assertEquals(8, repository.obtenerLugares().size)
        // 5 de riesgo + 2 distractores (sección 5.12 del maestro)
        assertEquals(7, repository.obtenerObjetosDeLugar("mi_cuarto").size)
    }

    @Test
    fun `sembrar dos veces no duplica los objetos`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        repository.sembrarSiEsPrimerLanzamiento()
        assertEquals(7, repository.obtenerObjetosDeLugar("mi_cuarto").size)
    }

    @Test
    fun `registrar una correccion de escena segura la guarda y otorga Primera Correccion`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val escenaSegura = Escena(
            id = "mi_cuarto",
            objetos = listOf(ObjetoRiesgo("cable_suelto", "Cable", corregido = true)),
        )
        val quedoSegura = repository.registrarCorreccion("mi_cuarto", escenaSegura)
        assertTrue(quedoSegura)
        assertTrue("primera_correccion" in db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `registrar una correccion de escena insegura no la guarda y queda pendiente de repaso`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val escenaInsegura = Escena(
            id = "mi_cuarto",
            objetos = listOf(ObjetoRiesgo("cable_suelto", "Cable", corregido = false)),
        )
        val quedoSegura = repository.registrarCorreccion("mi_cuarto", escenaInsegura)
        assertFalse(quedoSegura)
        assertTrue(db.correccionRegistradaDao().obtenerTodas().isEmpty())
    }

    @Test
    fun `evaluar un simulacro que pasa marca la insignia Simulacro Superado`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val escenaFinal = Escena(
            id = "simulacro_final",
            objetos = listOf(ObjetoRiesgo("fogata_final", "Fogata", corregido = true), ObjetoRiesgo("carpa_final", "Carpa", corregido = true)),
            reglasDistancia = listOf(ReglaDistancia("fogata_final", "carpa_final", distanciaMinimaCumplida = true)),
        )
        repository.evaluarSimulacroFinal(escenaFinal)
        assertTrue("simulacro_superado" in db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `evaluar un simulacro que no pasa no marca Simulacro Superado`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val escenaFinal = Escena(
            id = "simulacro_final",
            objetos = listOf(ObjetoRiesgo("fogata_final", "Fogata", corregido = true), ObjetoRiesgo("carpa_final", "Carpa", corregido = false)),
        )
        repository.evaluarSimulacroFinal(escenaFinal)
        assertFalse("simulacro_superado" in db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `pasar el simulacro final tambien lo cuenta como lugar corregido, igual que los otros 7`() = runTest {
        // Regresion: LugarViewModel enruta "simulacro_final" a evaluarSimulacroFinal, nunca a
        // registrarCorreccion -- si evaluarSimulacroFinal no anota tambien en
        // correccion_registrada, el Home nunca llega a "8 de 8" ni muestra Dominado, y
        // "Brigada Completa" (exige los 8) no se puede ganar nunca en la app real.
        repository.sembrarSiEsPrimerLanzamiento()
        val escenaFinal = Escena(
            id = "simulacro_final",
            objetos = listOf(ObjetoRiesgo("fogata_final", "Fogata", corregido = true), ObjetoRiesgo("carpa_final", "Carpa", corregido = true)),
            reglasDistancia = listOf(ReglaDistancia("fogata_final", "carpa_final", distanciaMinimaCumplida = true)),
        )
        repository.evaluarSimulacroFinal(escenaFinal)
        val idsCorregidos = db.correccionRegistradaDao().obtenerTodas().filter { it.escenaQuedoSegura }.map { it.lugarId }
        assertTrue("simulacro_final" in idsCorregidos)
    }

    @Test
    fun `pasar el simulacro final via su camino real completa la insignia Brigada Completa`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val lugares = repository.obtenerLugares()
        lugares.filter { it.id != "simulacro_final" }.forEach { lugar ->
            repository.registrarCorreccion(lugar.id, Escena(id = lugar.id, objetos = listOf(ObjetoRiesgo("x", "X", corregido = true))))
        }
        repository.evaluarSimulacroFinal(
            Escena(
                id = "simulacro_final",
                objetos = listOf(ObjetoRiesgo("fogata_final", "Fogata", corregido = true), ObjetoRiesgo("carpa_final", "Carpa", corregido = true)),
                reglasDistancia = listOf(ReglaDistancia("fogata_final", "carpa_final", distanciaMinimaCumplida = true)),
            ),
        )
        assertTrue("brigada_completa" in db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `corregir los 8 lugares distintos otorga Brigada Completa`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        repository.obtenerLugares().forEach { lugar ->
            val escenaSegura = Escena(id = lugar.id, objetos = listOf(ObjetoRiesgo("x", "X", corregido = true)))
            repository.registrarCorreccion(lugar.id, escenaSegura)
        }
        assertTrue("brigada_completa" in db.insigniaDao().obtenerIdsGanadas())
    }
}
