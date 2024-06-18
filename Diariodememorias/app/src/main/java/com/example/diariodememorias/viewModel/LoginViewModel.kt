package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import com.example.diariodememorias.repositorio.LoginRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repositorio: LoginRepositorio) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultadoLogin?>(null)
    var loginState: StateFlow<ResultadoLogin?> = _loginState

    fun entrar(email: String, senha: String) {
        if (email.isNotEmpty() || senha.isNotEmpty()) {
            repositorio.entrar(email, senha+"dias"){sucesso, msg ->
                _loginState.value = ResultadoLogin(sucesso, msg)
            }
        } else {
            _loginState.value = ResultadoLogin(false, "Preencha todos os campos")
        }
    }

    fun resetEstadoLogin() {
        _loginState.value = null
    }

}

data class ResultadoLogin(val sucesso: Boolean, val msg: String?)