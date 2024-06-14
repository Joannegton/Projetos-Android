package com.example.testemvvm.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitacoes_amizade")
data class SolicitacaoAmizadeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val status: String
)

