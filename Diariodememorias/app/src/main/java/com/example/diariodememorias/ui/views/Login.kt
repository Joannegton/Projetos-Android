package com.example.diariodememorias.ui.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.diariodememorias.MainActivity
import com.example.diariodememorias.R
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.ui.componentes.Titulo
import com.example.diariodememorias.ui.theme.DiarioDeMemoriasTheme
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel
import com.example.diariodememorias.viewModel.MemoriaViewModel
import com.example.diariodememorias.viewModel.ResultadoLogin

@Composable
fun Login(
    navController: NavController,
    viewModel: GerenciamentoSessaoViewModel,
    memoriaViewModel: MemoriaViewModel,
    onLoginSuccess: () -> Unit
) {
    // Estados mutáveis para os campos de entrada de email e senha
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Obtenha o contexto atual para exibir mensagens Toast
    val context = LocalContext.current
    // Observe o estado de login do ViewModel
    val loginState by viewModel.loginState.collectAsState()

    val usuarioLogado by viewModel.usuarioLogado().collectAsState(initial = false)


    LaunchedEffect(loginState?.sucesso) {
        if (loginState is ResultadoLogin) {
            if (loginState!!.sucesso) {
                onLoginSuccess()
            } else {
                // ...
            }
            viewModel.resetEstadoLogin()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Spacer(modifier = Modifier.padding(50.dp))

        Image(
            painter = painterResource(R.drawable.logo), contentDescription = "logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.padding(20.dp))


        Titulo(
            texto = "Realize seu Login"
        )
        Spacer(modifier = Modifier.padding(15.dp))

        // Campo de entrada de email
        EntradaTexto(
            texto = email.replace(" ", ""),
            onValueChange = { email = it },
            label = "email",
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de senha
        EntradaTexto(
            texto = senha.replace(" ", ""),
            onValueChange = { senha = it },
            label = "Senha",
            isSenha = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.padding(10.dp))

        // Botão de Login
        Botao(
            onClick = {
                viewModel.entrar(email, senha)
                (context as MainActivity).mostrarInterstitialAnuncio()
            },
            texto = "Entrar",
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.padding(15.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Não possui conta?", fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary)
            Text(
                text = "Cadastre-se",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { navController.navigate("cadastro") }
            )
        }

    }
    // Observe o estado de login e reaja às mudanças
    LaunchedEffect(loginState?.sucesso) { // A chave muda apenas quando o sucesso muda
        if (loginState is ResultadoLogin) {
            if (loginState!!.sucesso) {
                onLoginSuccess()
                memoriaViewModel.carregarMemorias()
            } else {
                Toast.makeText(context, loginState!!.msg, Toast.LENGTH_SHORT).show()
            }
            viewModel.resetEstadoLogin()
        }
    }

    LaunchedEffect(usuarioLogado) {
        if (usuarioLogado) {
            navController.navigate("diary")
        }
    }
}

@Preview
@Composable
private fun Previ() {
    DiarioDeMemoriasTheme {
        val gerenciadorViewModel: GerenciamentoSessaoViewModel = hiltViewModel()
        val memoriaVielModel: MemoriaViewModel = hiltViewModel()

        Login(
            navController = rememberNavController(),
            viewModel = gerenciadorViewModel,
            memoriaViewModel = memoriaVielModel
        ) {

        }
    }
}