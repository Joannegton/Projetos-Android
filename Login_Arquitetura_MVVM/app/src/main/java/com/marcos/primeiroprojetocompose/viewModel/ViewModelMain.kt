package com.marcos.primeiroprojetocompose.viewModel

import androidx.lifecycle.ViewModel
import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import com.marcos.primeiroprojetocompose.repositorio.RepositorioMain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(val repositorioMain: RepositorioMain) : ViewModel() {
    fun login(email: String, senha: String, respostaServidor: RespostaServidor){
        repositorioMain.login(email, senha, respostaServidor)
    }
}