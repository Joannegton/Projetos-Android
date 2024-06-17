package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import kotlin.math.roundToInt

@Composable
fun VisualizadorImagemUrl(
    imagePainter: ImagePainter,
    dialogVisivel: Boolean,
    onDialogDismiss: () -> Unit,
){
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) } // offset representa um deslocamento em relação à posição original de um elemento na tela.

    if (dialogVisivel) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .pointerInput(Unit) {//permite detectar gestos de transformação, como zoom e pan.
                detectTransformGestures { _, pan, zoom, _ -> //detecta gestos de transformação e atualiza os estados scale e offset de acordo.
                    scale = (scale * zoom).coerceAtLeast(1f)
                    if (scale > 1f) {
                        // Calcula o novo offset, restringindo o movimento para que a imagem não saia da tela
                        val maxX = (scale - 1f) * imagePainter.intrinsicSize.width / 2
                        val maxY = (scale - 1f) * imagePainter.intrinsicSize.height / 2
                        offset = Offset(
                            x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                            y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                        )
                    }
                }
            }
        ) {



            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) } // Aplica o offset calculado
                    .graphicsLayer { // Aplica a escala à imagem
                        scaleX = scale
                        scaleY = scale
                    }
            )

            // Botão para fechar o visualizador
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