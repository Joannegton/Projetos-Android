package com.example.buscadordecepapi.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.buscadordecepapi.listener.RespostaAPI
import com.example.buscadordecepapi.ui.componentes.Botao
import com.example.buscadordecepapi.ui.componentes.EntradaTexto
import com.example.buscadordecepapi.viewModel.BuscarCepViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscadorCep(navController: NavController, viewModel: BuscarCepViewModel = hiltViewModel()) {

    var inputCep by remember { mutableStateOf("") }
    var inputLogradouro by remember { mutableStateOf("") }
    var inputBairro by remember { mutableStateOf("") }
    var inputCidade by remember { mutableStateOf("") }
    var inputEstado by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EntradaTexto(
                value = inputCep,
                onValueChange = { novoCep ->
                    inputCep = novoCep
                },
                label = "Cep",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                modifier = Modifier
                    .width(200.dp)
            )
            Botao(
                texto = "Buscar Cep",
                onClick = {
                   viewModel.respostaApi(inputCep, object : RespostaAPI {
                       override fun onSucess(
                           logradouro: String,
                           bairro: String,
                           cidade: String,
                           uf: String
                       ) {
                           inputLogradouro = logradouro
                           inputBairro = bairro
                           inputCidade = cidade
                           inputEstado = uf
                       }

                       override fun onFailure(erro: String) {
                           Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                       }
                   })
                },
                modifier = Modifier
                    .height(57.dp)
                    .padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        EntradaTexto(
            value = inputLogradouro,
            onValueChange = { novoLogradouro -> inputLogradouro = novoLogradouro },
            label = "Logradouro",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(10.dp))
        EntradaTexto(
            value = inputBairro,
            onValueChange = { novoBairro -> inputBairro = novoBairro },
            label = "Bairro",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(10.dp))

        EntradaTexto(
            value = inputCidade,
            onValueChange = { novaCidade -> inputCidade = novaCidade },
            label = "Cidade",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(10.dp))

        EntradaTexto(
            value = inputEstado,
            onValueChange = { novoEstado -> inputEstado = novoEstado },
            label = "Estado",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            modifier = Modifier.width(150.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        Botao(
            texto = "Avançar",
            onClick = {
                if (inputCep.isEmpty() || inputLogradouro.isEmpty() || inputBairro.isEmpty() || inputCidade.isEmpty() || inputEstado.isEmpty()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else {
                    navController.navigate("detalhesEndereco/${inputLogradouro}/${inputBairro}/${inputCidade}/${inputEstado}")
                    
                }
            },
            modifier = Modifier
                .height(57.dp)
                .padding(start = 20.dp)
        )

    }
}
