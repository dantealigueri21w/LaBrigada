package pe.appmobile.labrigada.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun ZonaSoltar(
    modifier: Modifier = Modifier,
    onPosicionConocida: (Rect) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.onGloballyPositioned { onPosicionConocida(it.boundsInWindow()) }) {
        content()
    }
}

/**
 * Arrastra libremente con el dedo. Al soltar, si el CENTRO final cae dentro de
 * [zonaDestino], dispara [onSoltadaEnZona]; si no, vuelve a su posición original con una
 * animación de resorte (sección 5 del maestro, "Resorte al soltar").
 */
@Composable
fun FichaArrastrable(
    zonaDestino: Rect,
    onSoltadaEnZona: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var posicionDeReposo by remember { mutableStateOf(Rect.Zero) }
    val haptics = LocalHapticFeedback.current

    Box(
        modifier = modifier
            // onGloballyPositioned va ANTES (fuera) de .offset{} a propósito: así reporta la
            // posición de reposo de la ficha (su casilla real en el Row), estable durante todo
            // el arrastre -- nunca la posición ya desplazada, que puede llegar con retraso
            // respecto al valor de offset en un arrastre rápido y hacer fallar el soltado.
            .onGloballyPositioned { posicionDeReposo = it.boundsInWindow() }
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(zonaDestino) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    },
                    onDragEnd = {
                        val centroFinal = Offset(
                            posicionDeReposo.center.x + offset.x,
                            posicionDeReposo.center.y + offset.y,
                        )
                        if (zonaDestino.contains(centroFinal)) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSoltadaEnZona()
                        }
                        offset = Offset.Zero
                    },
                    onDragCancel = { offset = Offset.Zero },
                )
            },
    ) {
        content()
    }
}
