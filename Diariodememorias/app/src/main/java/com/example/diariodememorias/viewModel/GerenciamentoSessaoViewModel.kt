package com.example.diariodememorias.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.data.repositorio.GerenciadorDeSessao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GerenciamentoSessaoViewModel @Inject constructor(private val gerenciadorDeSessao: GerenciadorDeSessao
) : ViewModel() {

    private var _uidState = MutableStateFlow<String?>(null)
    val uidState: StateFlow<String?> = _uidState
    private var _uidParceiroState = MutableStateFlow<String?>(null)
    val uidParceiroState: StateFlow<String?> = _uidParceiroState

    init {
        viewModelScope.launch {
            try {
                gerenciadorDeSessao.obterUidFlow()
                    .collect { uid ->
                        _uidState.emit(uid)
                    }
            } catch (e: Exception) {
    Log.i("Tag", "errrroooo" + e.message.toString())           }
        }
    }

    fun pegarUidParceiro() {
        viewModelScope.launch {
            val uidParceiro = gerenciadorDeSessao.obterUidParceiro(uidState.value!!)
            if (uidParceiro != null) {
                _uidParceiroState.emit(uidParceiro)
                Log.i("Tag", "pegarUidParceiro: $uidParceiro")
            } else {
                Log.i("Tag", "pegarUidParceiro: null")
            }
        }
    }

    fun sair(){
        viewModelScope.launch {
            gerenciadorDeSessao.sair()
        }
    }
}