package com.stackmobile.aplicativodefilmes.ui.theme.repositorio

import com.stackmobile.aplicativodefilmes.ui.theme.model.Categoria
import com.stackmobile.aplicativodefilmes.ui.theme.servidor.Servidor
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class HomeRepositorio @Inject constructor(private val servidor: Servidor){

    suspend fun getCategorias(
        respostaDoServidor: (MutableList<Categoria>) -> Unit,
        mensagemErro: (String) -> Unit
    ){
        servidor.getCategorias(respostaDoServidor,mensagemErro)
    }
}