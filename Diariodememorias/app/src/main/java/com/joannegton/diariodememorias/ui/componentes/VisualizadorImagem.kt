package com.joannegton.diariodememorias.ui.componentes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlin.math.roundToInt

@OptIn(ExperimentalCoilApi::class)
@Composable
fun VisualizadorImagem(
    imagem: Uri?,
    dialog: Boolean,
    onDialogDismiss: () -> Unit
){
    var scale by remember { mutableFloatStateOf(2f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    if (dialog && imagem != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (scale > 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else{
                                scale = 2f
                            }
                        }
                    )
                }
        ) {
            Image(
                painter = rememberImagePainter(data = imagem),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceAtLeast(1f)
                            if (scale > 1f) {
                                val maxX = 100f // Substitua por um valor adequado
                                val maxY = 100f // Substitua por um valor adequado
                                offset = Offset(
                                    x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                                )
                            }
                        }
                    }
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "fechar",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 10.dp, end = 10.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onDialogDismiss() }
            )
        }
    }
}