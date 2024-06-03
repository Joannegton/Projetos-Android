package com.marcos.primeiroprojetocompose.repositorio

import com.marcos.primeiroprojetocompose.data.DataSource
import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class RepositorioMain @Inject constructor(private val dataSource: DataSource ) {

    fun login(email: String, senha: String, respostaServidor: RespostaServidor){
        dataSource.login(email, senha, respostaServidor )
    }
}