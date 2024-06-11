package com.example.diariodememorias.models


data class Memoria(
    val title: String = "",
    val description: String = "",
    val imageUri: String? = null,
    val usuarioId: String = "",
    val compartilhadoCom: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
