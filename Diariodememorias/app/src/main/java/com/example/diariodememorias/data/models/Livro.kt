package com.example.diariodememorias.data.models

data class Livro(
    val titulo: String = "",
    val paginas: List<Pagina> = listOf()
)

data class Pagina(
    val motivo: String = "",
    val descricao: String = "",
    val imageUri: Int? = null
)
