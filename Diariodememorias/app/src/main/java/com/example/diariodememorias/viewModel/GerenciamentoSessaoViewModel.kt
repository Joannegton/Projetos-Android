package com.example.diariodememorias.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diariodememorias.util.GerenciadorDeSessao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GerenciamentoSessaoViewModel @Inject constructor(private val gerenciadorDeSessao: GerenciadorDeSessao
) : ViewModel() {

    fun obterUid() {
        viewModelScope.launch {
            gerenciadorDeSessao.obterUidFlow().collect { uid ->
                if (uid != null) {
                    // Usuário está logado, use o UID
                } else {
                    // Usuário não está logado
                }
            }
        }
    }
}