package com.example.diariodememorias.ui.componentes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.diariodememorias.ui.theme.secondaryLight

@Composable
fun Titulo(texto: String) {
    Text(
        text = texto,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = secondaryLight
    )
}