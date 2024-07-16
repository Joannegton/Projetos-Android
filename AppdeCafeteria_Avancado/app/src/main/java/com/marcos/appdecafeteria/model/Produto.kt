package com.marcos.appdecafeteria.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Produto(
    val imagem: ImageVector? = null,
    val nome: String? = null,
    val iconColor: Color,
    val preco: String? = null
)
