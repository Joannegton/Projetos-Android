package com.joannegton.diariodememorias.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joannegton.diariodememorias.data.repositorio.GerenciadorDeSessaoRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GerenciamentoSessaoViewModel @Inject constructor(private val repositorio: GerenciadorDeSessaoRepositorio) : ViewModel() {
    private val _loginState = MutableStateFlow<ResultadoLogin?>(null)
    var loginState: StateFlow<ResultadoLogin?> = _loginState

    private var _cadastroState = MutableStateFlow(Result.success(""))
    var cadastroState: StateFlow<Result<String>> = _cadastroState.asStateFlow()


    fun cadastrar(nome: String, email: String, senha: String, confirmarSenha: String) {
        viewModelScope.launch {
            if (nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank() || senha != confirmarSenha) {
                _cadastroState.value = Result.failure(IllegalArgumentException("Preencha todos os campos corretamente"))
                return@launch // Retorna somente da coroutine atual
            }

            val resultado = repositorio.cadastrar(nome, email, senha)
            _cadastroState.value = resultado
        }

    }
    fun entrar(email: String, senha: String) {
        viewModelScope.launch {
            if (email.isBlank() || senha.isBlank()) {
                _loginState.value = ResultadoLogin(false, "Preencha todos os campos")
                return@launch
            }

            repositorio.entrar(email, senha) { sucesso, msg ->
                _loginState.value = ResultadoLogin(sucesso, msg)
            }
        }
    }
    fun resetEstadoLogin() {
        _loginState.value = null
    }

    fun usuarioLogado(): Flow<Boolean> {
        return repositorio.usuarioLogado()
    }

    fun sair() {
        repositorio.sair()
    }

    fun usuarioId(): String {
        return repositorio.obterUid()
    }
    fun parceiroId(): String? {
        return repositorio.obterUidParceiro()
    }


}

data class ResultadoLogin(val sucesso: Boolean, val msg: String?)