package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose.secondaryContainerLight
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight

@Composable
fun EntradaTexto(
    texto: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isSenha: Boolean = false,
    linhaUnica: Boolean = true,
    obrigatorio: Boolean = false,
    modifier: Modifier = Modifier
) {
    val senhaVisivel = remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf(obrigatorio) }

    OutlinedTextField(
        value = texto,
        onValueChange = {
            onValueChange(it)
            erro = it.isBlank()},
        label = { Text(text = label) },
        isError = erro,
        singleLine = linhaUnica,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = secondaryDark,
            unfocusedContainerColor = secondaryContainerLight,
            focusedTextColor = Color.Black,
            unfocusedTextColor = secondaryLight,
            cursorColor = Color.White,
            focusedLabelColor = secondaryLight,
            unfocusedLabelColor = secondaryLight,
            focusedBorderColor = secondaryLight,
            unfocusedBorderColor = secondaryDark
            ),
        visualTransformation = if (isSenha && !senhaVisivel.value) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isSenha) {
                val icone = if (senhaVisivel.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { senhaVisivel.value = !senhaVisivel.value }) {
                    Icon(imageVector = icone, contentDescription = if (senhaVisivel.value) "Ocultar senha" else "Mostrar senha")
                }

            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        )
    )
    if (erro) {
        Text("Este campo é obrigatório", textAlign = TextAlign.Center, color = Color.Red, modifier = Modifier.fillMaxWidth().padding(2.dp))
    }
}