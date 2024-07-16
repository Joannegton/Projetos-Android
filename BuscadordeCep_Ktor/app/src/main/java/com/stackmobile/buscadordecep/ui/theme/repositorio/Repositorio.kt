package com.stackmobile.buscadordecep.ui.theme.repositorio

import com.stackmobile.buscadordecep.ui.theme.datasource.Servidor
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repositorio @Inject constructor(private val servidor: Servidor) {

    suspend fun buscarCep(
        cep: String,
        respostaDoServidor: (String,String,String,String) -> Unit,
        mensagemErro: (String) -> Unit
    ){
        servidor.buscarCep(cep,respostaDoServidor,mensagemErro)
    }
}