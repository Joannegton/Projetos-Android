package com.example.testemvvm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.testemvvm.data.database.AppDatabase
import com.example.testemvvm.data.database.SolicitacaoAmizadeEntity
import com.example.testemvvm.data.repository.SolicitacaoAmizadeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitacaoAmizadeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "solicitacoes_amizade.db"
    ).build()

    private val repository: SolicitacaoAmizadeRepository

    init {
        val solicitacaoAmizadeDao = db.solicitacaoAmizadeDao()
        repository = SolicitacaoAmizadeRepository(solicitacaoAmizadeDao)
        viewModelScope.launch {
            repository.getTodasSolicitacoes().collect { solicitacoes ->
                _solicitacoesAmizade.value = solicitacoes
                atualizarHistorico()
            }
        }
    }

    private val _solicitacoesAmizade = MutableStateFlow<List<SolicitacaoAmizadeEntity>>(emptyList())
    val solicitacoesAmizade: StateFlow<List<SolicitacaoAmizadeEntity>> = _solicitacoesAmizade

    private val _historicoSolicitacoesAceitas = MutableStateFlow<List<SolicitacaoAmizadeEntity>>(emptyList())
    val historicoSolicitacoesAceitas: StateFlow<List<SolicitacaoAmizadeEntity>> = _historicoSolicitacoesAceitas

    fun enviarSolicitacaoAmizade(nome: String) {
        viewModelScope.launch {
            val novaSolicitacao = SolicitacaoAmizadeEntity(nome = nome, status = "Pendente")
            repository.inserirSolicitacao(novaSolicitacao)
        }
    }

    fun aceitarSolicitacaoAmizade(id: Int) {
        viewModelScope.launch {
            val solicitacao = _solicitacoesAmizade.value.find { it.id == id }
            solicitacao?.let {
                val atualizada = it.copy(status = "Aceita")
                repository.atualizarSolicitacao(atualizada)
            }
        }
    }

    fun removerSolicitacaoAmizade(id: Int) {
        viewModelScope.launch {
            val solicitacao = _solicitacoesAmizade.value.find { it.id == id }
            solicitacao?.let {
                repository.deletarSolicitacao(it)
            }
        }
    }

    private fun atualizarHistorico() {
        _historicoSolicitacoesAceitas.value = _solicitacoesAmizade.value.filter { it.status == "Aceita" }
    }
}
