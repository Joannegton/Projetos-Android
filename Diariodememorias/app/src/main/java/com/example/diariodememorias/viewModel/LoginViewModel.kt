package com.example.diariodememorias.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.repositorio.LoginRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repositorio: LoginRepositorio) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultadoLogin?>(null)
    var loginState: StateFlow<ResultadoLogin?> = _loginState

    private var _cadastroState = MutableStateFlow<Result<String>>(Result.success(""))
    var cadastroState: StateFlow<Result<String>> = _cadastroState.asStateFlow()

    fun entrar(email: String, senha: String) {
        if (email.isNotEmpty() || senha.isNotEmpty()) {
            repositorio.entrar(email, senha) { sucesso, msg ->
                _loginState.value = ResultadoLogin(sucesso, msg)
            }
        } else {
            _loginState.value = ResultadoLogin(false, "Preencha todos os campos")
        }
    }

    fun resetEstadoLogin() {
        _loginState.value = null
    }

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

}

data class ResultadoLogin(val sucesso: Boolean, val msg: String?)