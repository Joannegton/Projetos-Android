package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ConectarParceiroDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        title = { Text(text = "Solicitar Conexão", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth()) },
        text = {
            Column {
                Text("Insira o e-mail do parceiro", textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                EntradaTexto(
                    texto = email,
                    onValueChange = { email = it },
                    label = "Email do Parceiro",
                    imeAction = ImeAction.Done
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Botao(
                texto = "Enviar",
                onClick = { onConfirm(email); onDismiss() },
                enabled = email.isNotBlank(),
                largura = 130
            )
        },
        dismissButton = {
            Botao(
                onClick = onDismiss,
                texto = "Cancelar",
                largura = 155
            )
        },
        icon = { IconeConectar()},
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun Fun(modifier: Modifier = Modifier) {
    ConectarParceiroDialog(onConfirm = {}) {

    }
}