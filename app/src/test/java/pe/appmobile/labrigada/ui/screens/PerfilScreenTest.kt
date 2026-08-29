package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme
import pe.appmobile.labrigada.ui.viewmodel.PerfilUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PerfilScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `la pantalla de perfil no revienta con datos reales`() {
        compose.setContent {
            LaBrigadaTheme {
                PerfilScreen(
                    uiState = PerfilUiState(alias = "Brigadista Firu", avatarId = 0, cargando = false),
                    onAliasChange = {}, onAvatarChange = {}, onGuardar = {}, onVolver = {},
                )
            }
        }
    }

    @Test
    fun `elegir un avatar dispara el callback con su id`() {
        var idElegido: Int? = null
        compose.setContent {
            LaBrigadaTheme {
                PerfilScreen(
                    uiState = PerfilUiState(alias = "Brigadista Firu", avatarId = 0, cargando = false),
                    onAliasChange = {}, onAvatarChange = { idElegido = it }, onGuardar = {}, onVolver = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Brigadista con gorra").performClick()
        assertEquals(1, idElegido)
    }

    @Test
    fun `tocar guardar dispara el callback de guardado`() {
        var guardadoLlamado = false
        compose.setContent {
            LaBrigadaTheme {
                PerfilScreen(
                    uiState = PerfilUiState(alias = "Brigadista Firu", avatarId = 0, cargando = false),
                    onAliasChange = {}, onAvatarChange = {}, onGuardar = { guardadoLlamado = true }, onVolver = {},
                )
            }
        }
        compose.onNodeWithText("Guardar").performScrollTo().performClick()
        assert(guardadoLlamado)
    }
}
