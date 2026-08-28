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

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BrigadaRepositoryBitacoraTest {
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
    fun `sin correcciones la bitacora esta vacia`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        assertTrue(repository.obtenerBitacora().isEmpty())
    }

    @Test
    fun `una correccion segura aparece en la bitacora con el nombre real del lugar`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        db.correccionRegistradaDao().insertar(CorreccionRegistradaEntity(lugarId = "mi_cuarto", fecha = 1000L, escenaQuedoSegura = true))
        val bitacora = repository.obtenerBitacora()
        assertEquals(1, bitacora.size)
        assertEquals("Mi Cuarto", bitacora.first().nombreLugar)
    }

    @Test
    fun `la bitacora se ordena de la mas reciente a la mas antigua`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        db.correccionRegistradaDao().insertar(CorreccionRegistradaEntity(lugarId = "mi_cuarto", fecha = 1000L, escenaQuedoSegura = true))
        db.correccionRegistradaDao().insertar(CorreccionRegistradaEntity(lugarId = "la_cocina", fecha = 2000L, escenaQuedoSegura = true))
        val bitacora = repository.obtenerBitacora()
        assertEquals("La Cocina", bitacora.first().nombreLugar)
    }
}
