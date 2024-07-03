package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.secondaryDark

@Composable
fun Botao(
    onClick: () -> Unit,
    texto: String,
    largura: Int = 200,
    fonteTexto: Int = 20,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(secondaryDark),
        enabled = enabled,
        modifier = modifier
            .width(largura.dp)
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = fonteTexto.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}