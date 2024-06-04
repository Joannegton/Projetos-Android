package com.marcos.primeiroprojetocompose.data

import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import javax.inject.Inject

// A classe DataSource é usada para gerenciar os dados relacionados ao login
class DataSource @Inject constructor() {

    // O método login realiza a verificação das credenciais do usuário
    fun login(email: String, senha: String, respostaServidor: RespostaServidor) {

        // Verifica se algum dos campos de email ou senha está vazio
        if (email.isEmpty() || senha.isEmpty()) {
            // Se algum campo está vazio, aciona o callback de erro informando que todos os campos devem ser preenchidos
            respostaServidor.onError("Preencha todos os campos")
        }
        // Verifica se o email e a senha correspondem às credenciais corretas
        else if (email == "admin@admin.com" && senha == "1234") {
            // Se as credenciais estão corretas, aciona o callback de sucesso informando que o login foi realizado com sucesso
            respostaServidor.onSucess("Login realizado com sucesso")
        }
        // Se as credenciais não correspondem, aciona o callback de erro informando que o email ou senha estão incorretos
        else {
            respostaServidor.onError("Email ou senha incorretos")
        }
    }
}
