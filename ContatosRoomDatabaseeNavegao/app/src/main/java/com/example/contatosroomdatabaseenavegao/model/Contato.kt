package com.example.contatosroomdatabaseenavegao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contatosroomdatabaseenavegao.const.Constantes

@Entity(tableName = Constantes.TABELA_CONTATOS)
data class Contato (
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "sobrenome") val sobrenome: String,
    @ColumnInfo(name = "idade") val idade: String,
    @ColumnInfo(name = "celular") val celular: String,
){
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

