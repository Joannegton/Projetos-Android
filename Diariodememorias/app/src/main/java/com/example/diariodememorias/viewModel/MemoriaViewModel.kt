package com.example.diariodememorias.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.repositorio.ConexaoRepositorio
import com.example.diariodememorias.repositorio.MemoriaRepositorio
import com.example.diariodememorias.util.GerenciadorDeSessao
import com.example.diariodememorias.util.Resultado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriaViewModel @Inject constructor(private val repository: MemoriaRepositorio, private val repositorio: GerenciadorDeSessao) :
    ViewModel() {

    // Estado para armazenar a lista de memórias
    private val _memories = MutableStateFlow<List<Memoria>>(emptyList())
    val memories: StateFlow<List<Memoria>> = _memories

    // Estado para indicar se está carregando
    private val _isCarregando = MutableStateFlow(false)
    val isCarregando: StateFlow<Boolean> = _isCarregando


    // Estado para armazenar a mensagem de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Função para buscar memórias, utilizando coroutines para operações assíncronas
    fun pegarMemorias() {
        val usuarioId = repositorio.obterUidFlow()

        viewModelScope.launch {
            val parceiroId = repositorio.obterUidParceiro(usuarioId.toString())
            _isCarregando.value = true
            try {
                val fetchedMemories = repository.pegarMemorias(usuarioId.toString(), parceiroId.toString())
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
                repository.adicionarMemoria(memoria)
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
