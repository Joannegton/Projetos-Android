package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.repositorio.ConexaoRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConexaoViewModel @Inject constructor(private val repositorio: ConexaoRepositorio) : ViewModel() {

    private val _conexaoState = MutableStateFlow<ConexaoState>(ConexaoState.Idle)
    val conexaoState = _conexaoState.asStateFlow()

    fun conectarParceiro(usuarioId: String, emailParceiro: String) {
        viewModelScope.launch {
            _conexaoState.value = ConexaoState.Loading
            val resultado = repositorio.conectarParceiro(usuarioId, emailParceiro)
            _conexaoState.value = if (resultado.isSuccess) {
                Log.i("tag", "deu certo")
                ConexaoState.Success
            } else {
                Log.i("tag", "deu errado")
                ConexaoState.Error(resultado.exceptionOrNull()?.message)
            }
        }
    }

    
}

sealed class ConexaoState {
    object Idle : ConexaoState()
    object Loading : ConexaoState()
    object Success : ConexaoState()
    data class Error(val mensagem: String?) : ConexaoState()
}