package com.example.diariodememorias.data.models

data class Usuario(
    var id: String? = null,
    var nome: String? = null,
    var email: String? = null,
    val senha: String? = null,
    val parceiroId: String? = null)