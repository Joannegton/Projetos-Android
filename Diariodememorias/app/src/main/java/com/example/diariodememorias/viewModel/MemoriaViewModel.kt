package com.example.diariodememorias.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.repositorio.MemoriaRepositorio
import com.example.diariodememorias.util.Resultado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriaViewModel @Inject constructor(private val repository: MemoriaRepositorio) :
    ViewModel() {

    // Estado para armazenar a lista de memórias
    private val _memories = MutableStateFlow<List<Memoria>>(emptyList())
    val memories: StateFlow<List<Memoria>> = _memories

    // Estado para indicar se está carregando
    private val _isCarregando = MutableStateFlow(false)
    val isCarregando: StateFlow<Boolean> = _isCarregando

    // Estado para armazenar o resultado do envio de mídia
    private val _downloadUriResult = MutableStateFlow<Resultado<String>?>(null)
    val downloadUriResult: StateFlow<Resultado<String>?> = _downloadUriResult

    // Estado para armazenar o resultado da adição de memória
    private val _addMemoriaResult = MutableStateFlow<Result<Unit>?>(null)
    val addMemoriaResult: StateFlow<Result<Unit>?> = _addMemoriaResult

    // Estado para armazenar a mensagem de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getUsuarioId(): String {
        // Aqui você pode adicionar a lógica para buscar o ID do usuário do banco de dados
        // Supondo que você tenha uma função no seu repositório para buscar o ID do usuário
        return repository.getUsuarioId()
    }

    suspend fun getParceiroId(): String {
        // Aqui você pode adicionar a lógica para buscar o ID do parceiro do banco de dados
        // Supondo que você tenha uma função no seu repositório para buscar o ID do parceiro
        return repository.getParceiroId()
    }

    // Função para buscar memórias, utilizando coroutines para operações assíncronas
    fun pegarMemorias() {
        val usuarioId = getUsuarioId()
        viewModelScope.launch {
            val parceiroId = getParceiroId()
            _isCarregando.value = true
            try {
                val fetchedMemories = repository.pegarMemorias(usuarioId, parceiroId)
                _memories.value = fetchedMemories
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isCarregando.value = false
            }
        }
    }

    // Função para adicionar uma memória
    fun adicionarMemoria(memoria: Memoria) {
        viewModelScope.launch {
            _isCarregando.value = true
            try {
                val result = repository.adicionarMemoria(memoria)
                Log.i("TAG", "adicionarMemoria: $result")
                _addMemoriaResult.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isCarregando.value = false
            }
        }
    }

    // Função para enviar mídia
    suspend fun enviarMidia(uri: Uri): Resultado<String> {
        _isCarregando.value = true
        val resultado = repository.enviarMidia(uri)
        _isCarregando.value = false
        return resultado
    }
}
