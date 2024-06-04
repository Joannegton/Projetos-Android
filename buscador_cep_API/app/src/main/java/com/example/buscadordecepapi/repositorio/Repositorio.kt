package com.example.buscadordecepapi.repositorio

import com.example.buscadordecepapi.data.DataSource
import com.example.buscadordecepapi.listener.RespostaAPI
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repositorio @Inject constructor( private val dataSource: DataSource) {
    fun respostaApi(cep: String, respostaAPI: RespostaAPI){
        dataSource.respostaApi(cep, respostaAPI)
    }
}