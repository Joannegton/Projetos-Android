package com.example.diariodememorias.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.DiarioDeMemoriasTheme
import com.example.diariodememorias.repositorio.LoginRepositorio
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.ui.componentes.Titulo
import com.example.diariodememorias.viewModel.LoginViewModel

@Composable
fun Cadastro(navController: NavController, viewModel: LoginViewModel) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val cadastroState by viewModel.cadastroState.collectAsState()

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Titulo(texto = "Cadastrar")
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
            texto = "Cadastrar"
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
