package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.data.repositorio.GerenciadorDeSessaoRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
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


    init {
        viewModelScope.launch {
            try {
                repositorio.obterUidFlow()
                    .collect { uid ->
                        _uidState.emit(uid)
                    }
            } catch (e: Exception) {
    Log.i("Tag", "errrroooo" + e.message.toString())           }
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

    fun pegarUidParceiro() {
        viewModelScope.launch {
            val uidParceiro = repositorio.obterUidParceiro(uidState.value!!)
            if (uidParceiro != null) {
                _uidParceiroState.emit(uidParceiro)
                Log.i("Tag", "pegarUidParceiro: $uidParceiro")
                Log.i("Tag", "pegarUid: ${uidState.value}")
            } else {
                Log.i("Tag", "UidParceiro: null")
            }
        }
    }

    fun sair(){
        viewModelScope.launch {
            repositorio.sair()
        }
    }
}

data class ResultadoLogin(val sucesso: Boolean, val msg: String?)