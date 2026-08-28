package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.data.entity.LugarEntity
import pe.appmobile.labrigada.data.entity.ObjetoRiesgoEntity
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme
import pe.appmobile.labrigada.ui.viewmodel.LugarUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LugarScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val lugarDePrueba = LugarEntity("mi_cuarto", "Mi Cuarto", orden = 1)
    private val objetosDePrueba = listOf(
        ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto en el piso", 1, null),
        ObjetoRiesgoEntity("mueble_cerca_cama", "mi_cuarto", "Mueble pesado cerca de la cama", 2, null),
    )

    @Test
    fun `la pantalla de lugar no revienta con datos reales`() {
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(lugar = lugarDePrueba, objetos = objetosDePrueba, riesgosRestantes = 2, cargando = false),
                    onCorregirObjeto = {},
                )
            }
        }
    }

    @Test
    fun `tocar un objeto de conducta en La Calle dispara el callback con su id`() {
        var idCorregido: String? = null
        val objetosLaCalle = listOf(ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar sin mirar", 1, null))
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = LugarEntity("la_calle", "La Calle", orden = 5),
                        objetos = objetosLaCalle, riesgosRestantes = 1, cargando = false,
                    ),
                    onCorregirObjeto = { idCorregido = it },
                )
            }
        }
        compose.onNodeWithContentDescription("Toca para corregir: Cruzar sin mirar").performClick()
        assertEquals("cruzar_sin_mirar", idCorregido)
    }

    @Test
    fun `el mensaje de escena segura aparece cuando escenaSegura es verdadero`() {
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = lugarDePrueba, objetos = objetosDePrueba,
                        corregidos = setOf("cable_suelto", "mueble_cerca_cama"),
                        riesgosRestantes = 0, escenaSegura = true, cargando = false,
                    ),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithText("¡Firu confirma que el lugar quedó seguro!").assertExists()
    }

    @Test
    fun `pedir ayuda muestra una frase que nombra el objeto pendiente, sin dar la respuesta completa`() {
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(lugar = lugarDePrueba, objetos = objetosDePrueba, riesgosRestantes = 2, cargando = false),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Pedir una pista a Firu").performClick()
        compose.onNodeWithText("Firu dice: arrastra Cable suelto en el piso hasta el hueco que tiene su misma forma.").assertExists()
    }
}
