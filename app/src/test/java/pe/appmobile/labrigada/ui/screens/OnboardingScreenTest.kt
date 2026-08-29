package pe.appmobile.labrigada.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OnboardingScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `terminar el onboarding sin cambiar nada usa el alias por defecto y el primer avatar`() {
        var aliasFinal: String? = null
        var avatarFinal: Int? = null
        compose.setContent {
            LaBrigadaTheme {
                OnboardingScreen(
                    aliasPorDefecto = "Brigadista Firu",
                    onTerminar = { alias, avatar -> aliasFinal = alias; avatarFinal = avatar },
                )
            }
        }
        repeat(4) { compose.onNodeWithText("Continuar").performClick() }
        compose.onNodeWithText("Empezar").performScrollTo().performClick()
        assertEquals("Brigadista Firu", aliasFinal)
        assertEquals(0, avatarFinal)
    }

    @Test
    fun `el campo de alias arranca vacio, sin el nombre por defecto ya escrito`() {
        // Regresión: venía precargado con "Brigadista Firu", así que para poner el suyo el niño
        // tenía que borrarlo letra por letra. El alias por defecto se sigue aplicando al
        // terminar (lo cubre la prueba de arriba) y ahora se muestra como placeholder.
        var aliasFinal: String? = null
        compose.setContent {
            LaBrigadaTheme {
                OnboardingScreen(aliasPorDefecto = "Brigadista Firu", onTerminar = { alias, _ -> aliasFinal = alias })
            }
        }
        repeat(4) { compose.onNodeWithText("Continuar").performClick() }
        compose.onNodeWithText("Nombre de brigadista").performScrollTo().performTextInput("Rocío")
        compose.onNodeWithText("Empezar").performScrollTo().performClick()
        assertEquals("Rocío", aliasFinal)
    }

    @Test
    fun `elegir un avatar distinto en la ultima pagina se refleja en onTerminar`() {
        var avatarFinal: Int? = null
        compose.setContent {
            LaBrigadaTheme {
                OnboardingScreen(aliasPorDefecto = "Brigadista Firu", onTerminar = { _, avatar -> avatarFinal = avatar })
            }
        }
        repeat(4) { compose.onNodeWithText("Continuar").performClick() }
        compose.onNodeWithContentDescription("Insignia 2").performScrollTo().performClick()
        compose.onNodeWithText("Empezar").performScrollTo().performClick()
        assertEquals(1, avatarFinal)
    }
}
