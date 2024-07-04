package com.example.diariodememorias.ui.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.backgoundContainer
import com.example.diariodememorias.R
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.ui.componentes.Titulo
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel

@Composable
fun Cadastro(navController: NavController, viewModel: GerenciamentoSessaoViewModel) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val cadastroState by viewModel.cadastroState.collectAsState()

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Spacer(modifier = Modifier.padding(50.dp))

        Image(
            painter = painterResource(R.drawable.logo), contentDescription = "logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.padding(20.dp))

        Titulo(texto = "Cadastrar Usuário")
        Spacer(modifier = Modifier.height(16.dp))

        EntradaTexto(
            texto = nome,
            onValueChange = { nome = it },
            label = "Nome"
        )
        Spacer(modifier = Modifier.height(16.dp))

        EntradaTexto(
            texto = email,
            onValueChange = { email = it },
            label = "Email"
        )
        Spacer(modifier = Modifier.height(16.dp))

        EntradaTexto(
            texto = senha,
            onValueChange = { senha = it },
            isSenha = true,
            label = "Senha"
        )
        Spacer(modifier = Modifier.height(16.dp))


        EntradaTexto(
            texto = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            isSenha = true,
            label = "Confirmar Senha"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Botao(
            onClick = {
                viewModel.cadastrar(nome, email, senha, confirmarSenha)
            },
            texto = "Cadastrar",
            modifier = Modifier.fillMaxWidth()
        )

        // Observa o estado do cadastro e exibe o Toast
        LaunchedEffect(key1 = cadastroState) {
            if (cadastroState.isSuccess) {
                if (cadastroState.getOrNull()!! != ""){
                    Toast.makeText(
                        context,
                        cadastroState.getOrNull() ?: "Cadastro realizado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("login")
                }
            } else if (cadastroState.isFailure) {
                val mensagemErro = cadastroState.exceptionOrNull()?.message ?: "Erro ao cadastrar"
                Toast.makeText(
                    context,
                    mensagemErro,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
