package com.example.testemvvm.data.repository

import com.example.testemvvm.data.database.SolicitacaoAmizadeDao
import com.example.testemvvm.data.database.SolicitacaoAmizadeEntity
import kotlinx.coroutines.flow.Flow

class SolicitacaoAmizadeRepository(private val solicitacaoAmizadeDao: SolicitacaoAmizadeDao) {

    // Obtém todas as solicitações de amizade do banco de dados
    fun getTodasSolicitacoes(): Flow<List<SolicitacaoAmizadeEntity>> {
        return solicitacaoAmizadeDao.getTodasSolicitacoes()
    }

    // Insere uma nova solicitação de amizade no banco de dados
    suspend fun inserirSolicitacao(solicitacao: SolicitacaoAmizadeEntity) {
        solicitacaoAmizadeDao.inserirSolicitacao(solicitacao)
    }

    // Atualiza uma solicitação de amizade existente no banco de dados
    suspend fun atualizarSolicitacao(solicitacao: SolicitacaoAmizadeEntity) {
        solicitacaoAmizadeDao.atualizarSolicitacao(solicitacao)
    }

    // Remove uma solicitação de amizade do banco de dados
    suspend fun deletarSolicitacao(solicitacao: SolicitacaoAmizadeEntity) {
        solicitacaoAmizadeDao.deletarSolicitacao(solicitacao)
    }
}
