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
