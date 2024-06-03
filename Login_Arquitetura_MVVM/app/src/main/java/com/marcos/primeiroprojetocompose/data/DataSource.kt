package com.marcos.primeiroprojetocompose.data

import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import javax.inject.Inject

class DataSource @Inject constructor(){
    fun login(email: String, senha: String, respostaServidor: RespostaServidor){
        if (email.isEmpty() || senha.isEmpty()){
            respostaServidor.onError("Preencha todos os campos")
        } else if(email == "admin@admin.com" && senha == "1234"){
            respostaServidor.onSucess("Login realizado com sucesso")
        } else{
            respostaServidor.onError("Email ou senha incorretos")
        }
    }
}