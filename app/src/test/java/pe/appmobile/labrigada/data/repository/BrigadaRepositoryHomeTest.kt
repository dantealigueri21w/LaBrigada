package pe.appmobile.labrigada.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.AppDatabase
import pe.appmobile.labrigada.data.entity.CorreccionRegistradaEntity
import pe.appmobile.labrigada.data.entity.RepasoPendienteEntity
import pe.appmobile.labrigada.domain.model.EstadoLugar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BrigadaRepositoryHomeTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: BrigadaRepository

    @Before
    fun crearDb() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        repository = BrigadaRepository(db)
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `sin correcciones los 3 primeros lugares estan disponibles y los otros 5 bloqueados`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val lugares = repository.obtenerLugaresConEstado()
        assertEquals(8, lugares.size)
        assertEquals(3, lugares.count { it.estado != EstadoLugar.BLOQUEADO })
        assertEquals(5, lugares.count { it.estado == EstadoLugar.BLOQUEADO })
    }

    @Test
    fun `un lugar con correccion segura registrada aparece dominado`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val primerLugar = repository.obtenerLugaresConEstado().first().lugar.id
        db.correccionRegistradaDao().insertar(
            CorreccionRegistradaEntity(lugarId = primerLugar, fecha = 1000L, escenaQuedoSegura = true),
        )
        val actualizado = repository.obtenerLugaresConEstado().first { it.lugar.id == primerLugar }
        assertEquals(EstadoLugar.DOMINADO, actualizado.estado)
    }

    @Test
    fun `un lugar con un intento fallido registrado y sin corregir aparece iniciado`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val primerLugar = repository.obtenerLugaresConEstado().first().lugar.id
        db.repasoPendienteDao().guardar(
            RepasoPendienteEntity(itemId = primerLugar, fechaUltimoFallo = 1000L, intervaloDias = 1, proximaRevision = 2000L),
        )
        val actualizado = repository.obtenerLugaresConEstado().first { it.lugar.id == primerLugar }
        assertEquals(EstadoLugar.INICIADO, actualizado.estado)
    }

    @Test
    fun `completar 2 de los 3 iniciales desbloquea hasta el quinto lugar`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val tresIniciales = repository.obtenerLugaresConEstado().take(3).map { it.lugar.id }
        tresIniciales.take(2).forEach { id ->
            db.correccionRegistradaDao().insertar(CorreccionRegistradaEntity(lugarId = id, fecha = 1000L, escenaQuedoSegura = true))
        }
        val lugares = repository.obtenerLugaresConEstado()
        assertEquals(5, lugares.count { it.estado != EstadoLugar.BLOQUEADO })
    }

    @Test
    fun `un lugar bloqueado indica cuantos lugares mas hacen falta para abrirlo`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val cuarto = repository.obtenerLugaresConEstado()[3]
        assertTrue(cuarto.faltanParaAbrir > 0)
    }

    @Test
    fun `guardar y obtener perfil devuelve el mismo alias y avatar`() = runTest {
        repository.guardarPerfil(alias = "Brigadista Firu", avatarId = 3)
        val perfil = repository.obtenerPerfil()
        assertEquals("Brigadista Firu", perfil?.alias)
        assertEquals(3, perfil?.avatarId)
    }

    @Test
    fun `sin perfil guardado obtenerPerfil devuelve null`() = runTest {
        assertEquals(null, repository.obtenerPerfil())
    }
}
