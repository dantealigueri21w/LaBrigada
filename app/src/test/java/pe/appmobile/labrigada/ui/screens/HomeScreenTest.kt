package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.repository.LugarConEstado
import pe.appmobile.labrigada.domain.model.EstadoLugar
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme
import pe.appmobile.labrigada.ui.viewmodel.HomeUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HomeScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val lugarDisponible = LugarConEstado(LugarEntity("mi_cuarto", "Mi Cuarto", orden = 1), EstadoLugar.DISPONIBLE, 0)
    private val lugarBloqueado = LugarConEstado(LugarEntity("la_calle", "La Calle", orden = 5), EstadoLugar.BLOQUEADO, 2)

    @Test
    fun `la pantalla de inicio no revienta con datos reales`() {
        compose.setContent {
            LaBrigadaTheme {
                HomeScreen(
                    uiState = HomeUiState(lugares = listOf(lugarDisponible), cargando = false),
                    onLugarClick = {}, onBitacoraClick = {}, onPerfilClick = {},
                )
            }
        }
    }

    @Test
    fun `tocar un puesto de brigada disponible dispara la navegacion con su id`() {
        var idTocado: String? = null
        compose.setContent {
            LaBrigadaTheme {
                HomeScreen(
                    uiState = HomeUiState(lugares = listOf(lugarDisponible), cargando = false),
                    onLugarClick = { idTocado = it }, onBitacoraClick = {}, onPerfilClick = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Mi Cuarto, Disponible").performClick()
        assertEquals("mi_cuarto", idTocado)
    }

    @Test
    fun `tocar un puesto bloqueado no dispara navegacion y muestra que le falta`() {
        var idTocado: String? = null
        compose.setContent {
            LaBrigadaTheme {
                HomeScreen(
                    uiState = HomeUiState(lugares = listOf(lugarBloqueado), cargando = false),
                    onLugarClick = { idTocado = it }, onBitacoraClick = {}, onPerfilClick = {},
                )
            }
        }
        compose.onNodeWithContentDescription("La Calle, Bloqueado. Completa 2 lugar(es) más para abrir esto").performClick()
        assertNull(idTocado)
    }
}
