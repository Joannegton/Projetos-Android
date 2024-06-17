package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter

@Composable
fun VisualizadorImagemUrl(
    imagePainter: ImagePainter,
    dialog: Boolean,
    onDialogDismiss: () -> Unit
){
    if (dialog) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
        ) {

            var scale = remember { mutableStateOf(1f) }
            var offsetX = remember { mutableStateOf(0f) }
            var offsetY = remember { mutableStateOf(0f) }
            var transformOrigin = remember { mutableStateOf(TransformOrigin(0.5f, 0.5f)) }

            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer( //modificador de escala
                        scaleX = scale.value,
                        scaleY = scale.value,
                        translationX = offsetX.value,
                        translationY = offsetY.value,
                        transformOrigin = transformOrigin.value
                    )
                    .pointerInput(Unit){//permite lidar com eventos de entrada do ponteiro, como toques na tela.
                        detectTransformGestures { centroid, _, zoom, pan -> //permite detectar gestos de transformação, como pinça para zoom.
                            scale.value *= zoom
                            offsetX.value += pan
                            offsetY.value += pan
                            transformOrigin.value = TransformOrigin(centroid.x / size.width, centroid.y / size.height)
                        }
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX.value += dragAmount.x
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