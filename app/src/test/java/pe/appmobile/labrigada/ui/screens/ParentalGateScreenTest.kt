package pe.appmobile.labrigada.ui.screens

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.AppDatabase
import pe.appmobile.labrigada.data.repository.BrigadaRepository
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ParentalGateScreenTest {
    @get:Rule
    val compose = createComposeRule()

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
    fun `el gate cerrado no revienta y muestra la instruccion de mantener presionado`() {
        compose.setContent {
            LaBrigadaTheme {
                ParentalGateScreen(repository = repository)
            }
        }
        compose.onNodeWithText("Mantén presionado 3 segundos").assertExists()
    }
}
