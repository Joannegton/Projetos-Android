package com.example.buscadordecepapi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscadordecepapi.listener.RespostaAPI
import com.example.buscadordecepapi.repositorio.Repositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarCepViewModel @Inject constructor(private val repositorio: Repositorio) : ViewModel() {
    fun respostaApi(cep: String, respostaApi: RespostaAPI){
        viewModelScope.launch {
            repositorio.respostaApi(cep, respostaApi)
        }
    }
}