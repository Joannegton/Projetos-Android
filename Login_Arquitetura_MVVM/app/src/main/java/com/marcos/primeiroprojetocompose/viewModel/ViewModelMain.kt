package com.marcos.primeiroprojetocompose.viewModel

import androidx.lifecycle.ViewModel
import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import com.marcos.primeiroprojetocompose.repositorio.RepositorioMain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModelMain é uma classe que gerencia a UI-related data no ciclo de vida do ViewModel.
 * Utiliza Dagger Hilt para injeção de dependência, facilitando a instância de RepositorioMain.
 */
@HiltViewModel
class ViewModelMain @Inject constructor(private val repositorioMain: RepositorioMain) : ViewModel() {

    /**
     * Método login que delega a operação de login para o RepositorioMain.
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @param respostaServidor Um callback para fornecer o resultado da tentativa de login.
     */
    fun login(email: String, senha: String, respostaServidor: RespostaServidor) {
        // Delegar a operação de login para o RepositorioMain
        repositorioMain.login(email, senha, respostaServidor)
    }
}
