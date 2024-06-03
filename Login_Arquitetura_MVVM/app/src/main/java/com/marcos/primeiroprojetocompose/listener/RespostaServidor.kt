package com.marcos.primeiroprojetocompose.listener

interface RespostaServidor {
    fun onSucess(mensagem: String)
    fun onError(mensagem: String)
}