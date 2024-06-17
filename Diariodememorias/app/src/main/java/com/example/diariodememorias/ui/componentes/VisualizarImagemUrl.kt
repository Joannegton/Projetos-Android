package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter

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
            .clickable { onDialogDismiss() }
        ) {

            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "fechar",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 10.dp, end = 10.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}