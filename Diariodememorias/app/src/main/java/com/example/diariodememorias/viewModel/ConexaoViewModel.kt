package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.data.repositorio.ConexaoRepositorio
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
                ConexaoState.Success
            } else {
                Log.i("tag", "deu errado")
                ConexaoState.Error(resultado.exceptionOrNull()?.message)
            }
        }
    }

    
}

sealed class ConexaoState {
    data object Idle : ConexaoState()
    data object Loading : ConexaoState()
    data object Success : ConexaoState()
    data class Error(val mensagem: String?) : ConexaoState()
}