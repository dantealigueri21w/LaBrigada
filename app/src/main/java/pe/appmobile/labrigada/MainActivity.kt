package pe.appmobile.labrigada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pe.appmobile.labrigada.ui.navigation.NavGraph
import pe.appmobile.labrigada.ui.theme.LaBrigadaTheme

/**
 * El primer-lanzamiento se decide de forma asíncrona (LaunchedEffect), nunca con runBlocking en
 * onCreate: bloquear el hilo principal esperando a Room es la causa clásica de ANR al arrancar en
 * frío (sección 1.9 del archivo de lecciones -- viene literal así en el texto del plan).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LaBrigadaApplication
        setContent {
            LaBrigadaTheme {
                var esPrimerLanzamiento by remember { mutableStateOf<Boolean?>(null) }
                LaunchedEffect(Unit) {
                    esPrimerLanzamiento = app.repository.obtenerLugaresConEstado().isEmpty()
                }
                esPrimerLanzamiento?.let { primero ->
                    NavGraph(repository = app.repository, esPrimerLanzamiento = primero)
                }
            }
        }
    }
}
