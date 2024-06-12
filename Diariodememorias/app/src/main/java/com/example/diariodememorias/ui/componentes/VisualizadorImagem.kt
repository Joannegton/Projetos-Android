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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun VisualizadorImagem(
    imagem: Int,
    dialog: Boolean,
    onDialogDismiss: () -> Unit
){
    if (dialog) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable { onDialogDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "fechar",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 20.dp, end = 20.dp)
                    .align(Alignment.TopEnd)
            )

            Image(
                painter = painterResource(id = imagem),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}