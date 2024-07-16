package com.example.diariodememorias.data.models


data class Memoria(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUri: String? = null,
    val usuarioId: String? = null,
    val compartilhadoCom: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

