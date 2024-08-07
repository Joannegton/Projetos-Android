package com.example.diariodememorias.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.data.models.Memoria
import com.example.diariodememorias.data.repositorio.GerenciadorDeSessaoRepositorio
import com.example.diariodememorias.data.repositorio.MemoriaRepositorio
import com.example.diariodememorias.util.Resultado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriaViewModel @Inject constructor(
    private val repository: MemoriaRepositorio,
    private val gerenciadorDeSessao: GerenciadorDeSessaoRepositorio
) : ViewModel() {

    // Estado para indicar se está carregando
    private val _isCarregando = MutableStateFlow(false)
    val isCarregando: StateFlow<Boolean> = _isCarregando

    // Estado para armazenar a mensagem de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val todasMemorias = repository.memories
    private val _memories = MutableStateFlow<List<Memoria>>(emptyList())
    val memories: StateFlow<List<Memoria>> = _memories.asStateFlow()

    init {
        viewModelScope.launch {
            todasMemorias.collectLatest { memorias ->
                val usuarioId = gerenciadorDeSessao.obterUid()
                val parceiroId = gerenciadorDeSessao.obterUidParceiro()
                val memoriasFiltradas = memorias.filter {
                    it.usuarioId == usuarioId || it.compartilhadoCom == parceiroId
                }
                _memories.value = memoriasFiltradas
            }
        }
    }

    // Função para adicionar uma memória
    fun adicionarMemoria(memoria: Memoria) {
        viewModelScope.launch {
            _isCarregando.value = true
            try {
                repository.adicionarMemoria(memoria)
                //pegarMemorias()
                Log.d("TAG", "adicionarMemoria: $memoria")
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
