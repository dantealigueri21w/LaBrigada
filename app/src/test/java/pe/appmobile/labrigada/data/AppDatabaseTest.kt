package pe.appmobile.labrigada.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.entity.CorreccionRegistradaEntity
import pe.appmobile.labrigada.data.entity.InsigniaEntity
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.data.entity.RachaEntity
import pe.appmobile.labrigada.data.entity.RepasoPendienteEntity
import pe.appmobile.labrigada.data.entity.SimulacroResultadoEntity

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppDatabaseTest {
    private lateinit var db: AppDatabase

    @Before
    fun crearDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `base de datos recien creada no tiene objetos de riesgo`() = runTest {
        assertTrue(db.objetoRiesgoDao().obtenerPorLugar("mi_cuarto").isEmpty())
    }

    @Test
    fun `insertar y leer objetos de un lugar los devuelve ordenados`() = runTest {
        db.lugarDao().insertarTodos(listOf(LugarEntity("mi_cuarto", "Mi Cuarto", 1)))
        db.objetoRiesgoDao().insertarTodos(listOf(
            ObjetoRiesgoEntity("ventana_sin_seguro", "mi_cuarto", "Ventana sin seguro", 3, null),
            ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto en el piso", 1, null),
        ))
        val objetos = db.objetoRiesgoDao().obtenerPorLugar("mi_cuarto")
        assertEquals(2, objetos.size)
        assertEquals("cable_suelto", objetos.first().id)
    }

    @Test
    fun `un objeto con distancia minima guarda el id del objeto de referencia`() = runTest {
        db.lugarDao().insertarTodos(listOf(LugarEntity("el_campamento", "El Campamento", 7)))
        db.objetoRiesgoDao().insertarTodos(listOf(
            ObjetoRiesgoEntity("fogata", "el_campamento", "Fogata encendida", 1, "carpa"),
            ObjetoRiesgoEntity("carpa", "el_campamento", "Carpa armada", 2, null),
        ))
        val fogata = db.objetoRiesgoDao().obtenerPorLugar("el_campamento").first { it.id == "fogata" }
        assertEquals("carpa", fogata.distanciaMinimaDeId)
    }

    @Test
    fun `borrar un lugar borra en cascada sus objetos de riesgo`() = runTest {
        db.lugarDao().insertarTodos(listOf(LugarEntity("mi_cuarto", "Mi Cuarto", 1)))
        db.objetoRiesgoDao().insertarTodos(listOf(ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto", 1, null)))
        db.lugarDao().eliminar("mi_cuarto")
        assertTrue(db.objetoRiesgoDao().obtenerPorLugar("mi_cuarto").isEmpty())
    }

    @Test
    fun `una correccion insertada queda en el historial`() = runTest {
        db.lugarDao().insertarTodos(listOf(LugarEntity("mi_cuarto", "Mi Cuarto", 1)))
        db.correccionRegistradaDao().insertar(CorreccionRegistradaEntity(lugarId = "mi_cuarto", fecha = 1000L, escenaQuedoSegura = true))
        assertEquals(1, db.correccionRegistradaDao().obtenerTodas().size)
    }

    @Test
    fun `un resultado de simulacro insertado guarda los objetos que fallaron`() = runTest {
        db.lugarDao().insertarTodos(listOf(LugarEntity("simulacro_final", "El Simulacro Final", 8)))
        db.simulacroResultadoDao().insertar(
            SimulacroResultadoEntity(lugarId = "simulacro_final", fecha = 1000L, paso = false, objetosQueFallaronCsv = "cable_suelto_final,carpa_final"),
        )
        val resultado = db.simulacroResultadoDao().obtenerTodos().first()
        assertEquals("cable_suelto_final,carpa_final", resultado.objetosQueFallaronCsv)
    }

    @Test
    fun `marcar una insignia como obtenida la refleja en los ids ganados`() = runTest {
        db.insigniaDao().insertarTodas(listOf(InsigniaEntity("primera_correccion", "Primera Corrección", "Corregir la primera escena", fechaObtenida = null)))
        assertTrue(db.insigniaDao().obtenerIdsGanadas().isEmpty())
        db.insigniaDao().marcarObtenida("primera_correccion", fecha = 5000L)
        assertEquals(listOf("primera_correccion"), db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `guardar la racha dos veces reemplaza el valor anterior`() = runTest {
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = 1, ultimaFechaActividad = 1000L))
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = 2, ultimaFechaActividad = 2000L))
        assertEquals(2, db.rachaDao().obtener()?.diasConsecutivos)
    }

    @Test
    fun `sin racha guardada todavia obtener devuelve null`() = runTest {
        assertNull(db.rachaDao().obtener())
    }

    @Test
    fun `un item de repaso solo aparece pendiente para hoy cuando su fecha ya llego`() = runTest {
        val unDia = 24L * 60 * 60 * 1000
        db.repasoPendienteDao().guardar(RepasoPendienteEntity("mi_cuarto", fechaUltimoFallo = 0L, intervaloDias = 1, proximaRevision = unDia))
        assertTrue(db.repasoPendienteDao().obtenerPendientesParaHoy(hoy = unDia - 1000).isEmpty())
        assertEquals(1, db.repasoPendienteDao().obtenerPendientesParaHoy(hoy = unDia).size)
    }
}
