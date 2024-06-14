package com.example.testemvvm.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitacaoAmizadeDao {
    @Query("SELECT * FROM solicitacoes_amizade")
    fun getTodasSolicitacoes(): Flow<List<SolicitacaoAmizadeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirSolicitacao(solicitacao: SolicitacaoAmizadeEntity)

    @Update
    suspend fun atualizarSolicitacao(solicitacao: SolicitacaoAmizadeEntity)

    @Delete
    suspend fun deletarSolicitacao(solicitacao: SolicitacaoAmizadeEntity)
}
