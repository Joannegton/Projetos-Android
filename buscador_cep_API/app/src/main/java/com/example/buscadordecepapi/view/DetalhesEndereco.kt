package com.example.buscadordecepapi.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetalhesEndereco(
    navController: NavController,
    logradouro: String,
    bairro: String,
    cidade: String,
    estado: String

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = logradouro)
        Text(text = bairro)
        Text(text = cidade)
        Text(text = estado)
    }
}