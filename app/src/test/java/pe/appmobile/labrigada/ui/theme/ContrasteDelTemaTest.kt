package pe.appmobile.labrigada.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.math.max
import kotlin.math.min

/**
 * Regresión del bug de texto invisible: la app se veía como una pantalla en blanco (o con
 * letras que desaparecían) porque un par fondo/texto salía de esquemas distintos, o porque un
 * color oscurecido para leerse sobre blanco se usaba tal cual sobre el azul marino del modo
 * oscuro. Cada par que la app usa junto tiene que cumplir el mínimo de 4.5:1 de la sección 6 en
 * LOS DOS modos, y eso se verifica aquí en vez de mirándolo a ojo.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ContrasteDelTemaTest {
    @get:Rule
    val compose = createComposeRule()

    /** Fórmula de contraste de WCAG 2.1 sobre la luminancia relativa de cada color. */
    private fun contraste(a: Color, b: Color): Double {
        val la = a.luminance() + 0.05
        val lb = b.luminance() + 0.05
        return max(la, lb) / min(la, lb)
    }

    private fun paresDelTema(oscuro: Boolean): List<Triple<String, Color, Color>> {
        lateinit var pares: List<Triple<String, Color, Color>>
        compose.setContent {
            LaBrigadaTheme(darkTheme = oscuro) {
                val esquema = MaterialTheme.colorScheme
                pares = listOf(
                    Triple("texto sobre el fondo", esquema.onBackground, esquema.background),
                    Triple("texto sobre la superficie", esquema.onSurface, esquema.surface),
                    Triple("texto dentro de un botón", esquema.onPrimary, esquema.primary),
                    Triple("pista de Firu sobre el fondo", esquema.secondary, esquema.background),
                    Triple("mensaje de error sobre el fondo", esquema.error, esquema.background),
                    Triple("confirmación sobre el fondo", MaterialTheme.coloresDeApoyo.exito, esquema.background),
                    Triple("texto sobre el amarillo de aviso", esquema.onTertiary, esquema.tertiary),
                    Triple("texto dentro de una tarjeta", esquema.onSurfaceVariant, esquema.surfaceContainerLow),
                    // Sección 6.1 del maestro: el acento sobre el fondo, no solo el par
                    // ColorScheme que se aprueba solo porque onX se eligió junto a X.
                    Triple("acento primario sobre el fondo", esquema.primary, esquema.background),
                    Triple("acento secundario sobre el fondo", esquema.secondary, esquema.background),
                    Triple("halo de advertencia sobre el fondo", MaterialTheme.coloresDeApoyo.advertencia, esquema.background),
                )
            }
        }
        return pares
    }

    @Test
    fun `en modo claro todos los pares de color que la app usa juntos llegan a 4,5 a 1`() {
        paresDelTema(oscuro = false).forEach { (nombre, frente, fondo) ->
            val medido = contraste(frente, fondo)
            assertTrue("$nombre en modo claro: %.2f:1, por debajo de 4.5:1".format(medido), medido >= 4.5)
        }
    }

    @Test
    fun `en modo oscuro todos los pares de color que la app usa juntos llegan a 4,5 a 1`() {
        paresDelTema(oscuro = true).forEach { (nombre, frente, fondo) ->
            val medido = contraste(frente, fondo)
            assertTrue("$nombre en modo oscuro: %.2f:1, por debajo de 4.5:1".format(medido), medido >= 4.5)
        }
    }
}
