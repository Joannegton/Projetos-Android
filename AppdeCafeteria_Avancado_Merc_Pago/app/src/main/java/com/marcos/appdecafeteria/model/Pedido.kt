package com.marcos.appdecafeteria.model


data class Pedido(
    val numero: String? = null,
    val produto: String? = null,
    val preco: String? = null,
    val statusPagamento: String? = null
)
