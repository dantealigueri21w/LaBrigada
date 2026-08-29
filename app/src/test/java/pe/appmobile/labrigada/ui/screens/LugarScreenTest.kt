package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        ObjetoRiesgoEntity("cable_suelto", "mi_cuarto", "Cable suelto en el piso", 1, null, esRiesgo = true),
        ObjetoRiesgoEntity("mueble_cerca_cama", "mi_cuarto", "Mueble pesado cerca de la cama", 2, null, esRiesgo = true),
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
        val objetosLaCalle = listOf(ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar sin mirar", 1, null, esRiesgo = true))
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
        compose.onNodeWithText("¡Firu confirma que el lugar quedó seguro!").performScrollTo().assertExists()
    }

    @Test
    fun `el nombre de cada objeto se ve en pantalla, no solo en la descripcion accesible`() {
        // Regresión: el nombre vivía únicamente en el contentDescription, así que estaba
        // disponible para el lector de pantalla y no para quien mira -- varios objetos comparten
        // familia visual y no se sabía cuál era cuál.
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(lugar = lugarDePrueba, objetos = objetosDePrueba, riesgosRestantes = 2, cargando = false),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onAllNodesWithText("Cable suelto en el piso").onFirst().assertExists()
    }

    @Test
    fun `corregir un objeto deja un mensaje que dice cual fue`() {
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = lugarDePrueba, objetos = objetosDePrueba,
                        corregidos = setOf("cable_suelto"), riesgosRestantes = 1,
                        ultimoCorregidoId = "cable_suelto", cargando = false,
                    ),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithText("Listo: Cable suelto en el piso ya está en su lugar seguro.").assertExists()
    }

    @Test
    fun `tocar un distractor no dice que quedo corregido, dice que ya estaba bien`() {
        // Sección 5.12 del maestro: un distractor (esRiesgo = false) nunca entra a
        // "corregidos" en el ViewModel real, así que esta es exactamente la señal que la
        // pantalla usa para elegir el mensaje -- ultimoCorregidoId apunta a un id que NO está
        // en corregidos.
        val objetosConDistractor = objetosDePrueba + ObjetoRiesgoEntity(
            "cable_guardado", "mi_cuarto", "Cable enrollado y guardado", 3, null, esRiesgo = false,
        )
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = lugarDePrueba, objetos = objetosConDistractor, riesgosRestantes = 2,
                        ultimoCorregidoId = "cable_guardado", cargando = false,
                    ),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithText("Cable enrollado y guardado ya estaba bien. No hacía falta corregirlo.").assertExists()
    }

    @Test
    fun `tocar un distractor dispara el callback con su id, igual que un objeto real`() {
        val objetosConDistractor = objetosDePrueba + ObjetoRiesgoEntity(
            "cable_guardado", "mi_cuarto", "Cable enrollado y guardado", 3, null, esRiesgo = false,
        )
        var idTocado: String? = null
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(lugar = lugarDePrueba, objetos = objetosConDistractor, riesgosRestantes = 2, cargando = false),
                    onCorregirObjeto = { idTocado = it },
                )
            }
        }
        compose.onNodeWithContentDescription("Objeto seguro, no hace falta corregirlo: Cable enrollado y guardado").performScrollTo().performClick()
        assertEquals("cable_guardado", idTocado)
    }

    @Test
    fun `corregir una conducta no dice que quedo guardada en su lugar`() {
        // Una conducta se deja de hacer, no se guarda: con la frase de los objetos salía "Cruzar
        // sin mirar ya está en su lugar seguro".
        val objetosLaCalle = listOf(ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar sin mirar", 1, null, esRiesgo = true))
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = LugarEntity("la_calle", "La Calle", orden = 5),
                        objetos = objetosLaCalle, riesgosRestantes = 1,
                        ultimoCorregidoId = "cruzar_sin_mirar", cargando = false,
                    ),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithText("Bien pensado: Cruzar sin mirar ya no es un riesgo.").assertExists()
    }

    @Test
    fun `el boton de volver de la cabecera devuelve al mapa`() {
        var volvio = false
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(lugar = lugarDePrueba, objetos = objetosDePrueba, riesgosRestantes = 2, cargando = false),
                    onCorregirObjeto = {},
                    onVolver = { volvio = true },
                )
            }
        }
        compose.onNodeWithContentDescription("Volver al mapa del cuartel").performClick()
        assertTrue(volvio)
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
        compose.onNodeWithText("Firu dice: mantén presionado Cable suelto en el piso y arrástralo hasta el hueco que tiene su misma forma.").assertExists()
    }

    @Test
    fun `pedir ayuda sobre un objeto de conducta dice tocar, no arrastrar`() {
        // Regresion: la ayuda decia "arrastra X hasta el hueco" incluso para los objetos de La
        // Calle que se corrigen tocando, que no tienen ni zona de destino ni hueco alguno.
        val objetosLaCalle = listOf(ObjetoRiesgoEntity("cruzar_sin_mirar", "la_calle", "Cruzar sin mirar", 1, null, esRiesgo = true))
        compose.setContent {
            LaBrigadaTheme {
                LugarScreen(
                    uiState = LugarUiState(
                        lugar = LugarEntity("la_calle", "La Calle", orden = 5),
                        objetos = objetosLaCalle, riesgosRestantes = 1, cargando = false,
                    ),
                    onCorregirObjeto = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Pedir una pista a Firu").performClick()
        compose.onNodeWithText("Firu dice: toca Cruzar sin mirar para corregirlo.").assertExists()
    }
}
