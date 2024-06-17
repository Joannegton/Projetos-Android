package com.example.testemvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testemvvm.data.database.SolicitacaoAmizadeEntity
import com.example.testemvvm.viewModel.SolicitacaoAmizadeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitacaoAmizadeTela(viewModel: SolicitacaoAmizadeViewModel = viewModel()) {
    val solicitacoesAmizade by viewModel.solicitacoesAmizade.collectAsState()
    val historicoSolicitacoesAceitas by viewModel.historicoSolicitacoesAceitas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solicitações de Amizade") })
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                EnviarSolicitacaoSection(onEnviar = { nome -> viewModel.enviarSolicitacaoAmizade(nome) })
                Spacer(modifier = Modifier.height(16.dp))
                SolicitarAmizadeLista(
                    solicitacoes = solicitacoesAmizade,
                    onAceitar = { id -> viewModel.aceitarSolicitacaoAmizade(id) },
                    onRemover = { id -> viewModel.removerSolicitacaoAmizade(id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                HistoricoSolicitacoesAceitasLista(historico = historicoSolicitacoesAceitas)
            }
        }
    )
}

@Composable
fun EnviarSolicitacaoSection(onEnviar: (String) -> Unit) {
    var nome by remember { mutableStateOf(TextFieldValue()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        BasicTextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        .padding(16.dp)
                ) {
                    if (nome.text.isEmpty()) {
                        Text("Nome do amigo")
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onEnviar(nome.text) }) {
            Text("Enviar Solicitação")
        }
    }
}

@Composable
fun SolicitarAmizadeLista(
    solicitacoes: List<SolicitacaoAmizadeEntity>,
    onAceitar: (Int) -> Unit,
    onRemover: (Int) -> Unit
) {
    Column {
        solicitacoes.forEach { solicitacao ->
            SolicitacaoAmizadeItem(
                solicitacao = solicitacao,
                onAceitar = { onAceitar(solicitacao.id) },
                onRemover = { onRemover(solicitacao.id) }
            )
        }
    }
}

@Composable
fun HistoricoSolicitacoesAceitasLista(historico: List<SolicitacaoAmizadeEntity>) {
    Column {
        Text("Solicitações Aceitas:")
        historico.forEach { solicitacao ->
            Text(solicitacao.nome)
        }
    }
}



@Composable
fun SolicitacaoAmizadeItem(
    solicitacao: SolicitacaoAmizadeEntity,
    onAceitar: () -> Unit,
    onRemover: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${solicitacao.nome} - ${solicitacao.status}")
        Row {
            if (solicitacao.status == "Pendente") {
                Button(onClick = onAceitar) {
                    Text("Aceitar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onRemover) {
                    Text("Remover")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSolicitacaoAmizadeTela() {
    SolicitacaoAmizadeTela()
}
