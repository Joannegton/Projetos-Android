package com.example.listadecompras.componets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@Composable
fun MensagemErro(
    mensagem: String,
    larguraContainer: Int,
    isVisible: Boolean
){
    if (isVisible) {  // Verifique se a mensagem deve ser exibida com base em isVisible
        Text(
            text = mensagem,
            modifier = Modifier
                .width(larguraContainer.dp)
                .height(20.dp)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 10.dp)
                .animateContentSize(), //anima a transição do tamanho do conteúdo quando ele é alterado
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.error)
        )
    }
}
@Preview
@Composable
fun MensagemErroPreview(){
    MensagemErro("Insira um produto!", 20, true)
}

