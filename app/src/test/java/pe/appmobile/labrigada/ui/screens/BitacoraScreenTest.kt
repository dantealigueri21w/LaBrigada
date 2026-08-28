package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.repository.ItemBitacora
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme
import pe.appmobile.labrigada.ui.viewmodel.BitacoraUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BitacoraScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `la bitacora vacia muestra el mensaje de invitacion a empezar`() {
        compose.setContent {
            LaBrigadaTheme { BitacoraScreen(uiState = BitacoraUiState(items = emptyList(), cargando = false), onVolver = {}) }
        }
        compose.onNodeWithText("Todavía no corregiste ninguna escena. ¡Empieza por Mi Cuarto!").assertExists()
    }

    @Test
    fun `un item real de la bitacora muestra el nombre del lugar corregido`() {
        compose.setContent {
            LaBrigadaTheme {
                BitacoraScreen(
                    uiState = BitacoraUiState(items = listOf(ItemBitacora("Mi Cuarto", 1000L)), cargando = false),
                    onVolver = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Mi Cuarto quedó seguro").assertExists()
    }
}
