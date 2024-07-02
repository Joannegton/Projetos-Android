package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.data.repositorio.GerenciadorDeSessaoRepositorio
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

    private var _cadastroState = MutableStateFlow<Result<String>>(Result.success(""))
    var cadastroState: StateFlow<Result<String>> = _cadastroState.asStateFlow()

    private var _uidState = MutableStateFlow<String?>(null)
    val uidState: StateFlow<String?> = _uidState

    private var _uidParceiroState = MutableStateFlow<String?>(null)
    val uidParceiroState: StateFlow<String?> = _uidParceiroState

    private var _nomeState = MutableStateFlow<String?>(null)
        val nomeState: StateFlow<String?> = _nomeState

    private var _emailState = MutableStateFlow<String?>(null)
        val emailState: StateFlow<String?> = _emailState

    private val _sairUsuario = MutableStateFlow(false)
    val sairUsuario: StateFlow<Boolean> = _sairUsuario.asStateFlow()


    init {
        viewModelScope.launch {
//            repositorio.usuarioLogado.collect { usuario ->
//                _uidState.value = usuario?.id
//                _uidParceiroState.value = usuario?.parceiroId
//                _nomeState.value = usuario?.nome
//                _emailState.value = usuario?.email
//            }
        }
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

    fun sair() {
        repositorio.sair()
    }

    fun resetSairUsuario() {
        _sairUsuario.value = false // Redefine o estado _sairUsuario
    }

    fun usuarioLogado(): Flow<Boolean> {
        return repositorio.usuarioLogado()
    }
}

data class ResultadoLogin(val sucesso: Boolean, val msg: String?)