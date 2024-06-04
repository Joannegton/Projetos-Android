package com.example.buscadordecepapi.listener

interface RespostaAPI {
    fun onSucess(logradouro: String, bairro: String, cidade: String, uf: String)
    fun onFailure(erro: String)

}