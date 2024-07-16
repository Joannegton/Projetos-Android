package com.stackmobile.aplicativodefilmes.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stackmobile.aplicativodefilmes.ui.theme.model.Categoria
import com.stackmobile.aplicativodefilmes.ui.theme.repositorio.HomeRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepositorio: HomeRepositorio): ViewModel() {

    fun getCategorias(
        respostaDoServidor: (MutableList<Categoria>) -> Unit,
        mensagemErro: (String) -> Unit
    ){
        viewModelScope.launch {
            homeRepositorio.getCategorias(respostaDoServidor,mensagemErro)
        }
    }
}