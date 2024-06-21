package com.example.diariodememorias.data.models

data class Usuario(val id: String, val nome: String, val email: String, val senha: String, val parceiroId: String? = null)