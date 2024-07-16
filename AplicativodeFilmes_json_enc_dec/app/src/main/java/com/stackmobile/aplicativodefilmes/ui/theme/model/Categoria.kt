package com.stackmobile.aplicativodefilmes.ui.theme.model

data class Categoria(
   val titulo: String? = null,
   val filmes: MutableList<Filme> = mutableListOf()
)

data class Filme(
    val capa: String? = null,
    val id: Int = 0,
    val nome: String? = null,
    val elenco: String? = null,
    val descricao: String? = null
)

