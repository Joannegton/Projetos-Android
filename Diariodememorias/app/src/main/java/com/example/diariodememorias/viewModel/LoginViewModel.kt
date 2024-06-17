package com.example.diariodememorias.viewModel

import androidx.lifecycle.ViewModel
import com.example.diariodememorias.repositorio.LoginRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repositorio: LoginRepositorio) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultadoLogin?>(null)
    val loginState: StateFlow<ResultadoLogin?> = _loginState

    fun entrar(email: String, senha: String) {
        repositorio.entrar(email, senha+"dias"){sucesso, erro ->
            _loginState.value = ResultadoLogin(sucesso, erro)
        }
    }
}

data class ResultadoLogin(val sucesso: Boolean, val erro: String?)