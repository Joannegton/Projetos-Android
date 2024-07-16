package com.joannegton.diariodememorias.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
                Spacer(modifier = Modifier.height(8.dp))
                EntradaTexto(
                    texto = email.replace(" ", ""),
                    onValueChange = { email = it },
                    label = "Email do Parceiro",
                    imeAction = ImeAction.Done
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Row {
                Botao(
                    onClick = onDismiss,
                    texto = "Voltar",
                    largura = 130,
                    fonteTexto = 18,
                    cor = MaterialTheme.colorScheme.tertiaryContainer
                )
                Botao(
                    texto = "Enviar",
                    onClick = { onConfirm(email); onDismiss() },
                    enabled = email.isNotBlank(),
                    largura = 130,
                    fonteTexto = 18
                )

            }
        },
        icon = { IconeConectar() },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun Fun(modifier: Modifier = Modifier) {
    ConectarParceiroDialog(onConfirm = {}) {

    }
}