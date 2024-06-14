package com.marcos.primeiroprojetocompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marcos.primeiroprojetocompose.listener.RespostaServidor
import com.marcos.primeiroprojetocompose.ui.theme.PrimeiroProjetoComposeTheme
import com.marcos.primeiroprojetocompose.ui.theme.Purple500
import com.marcos.primeiroprojetocompose.viewModel.ViewModelMain
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity é a atividade principal que utiliza o Dagger Hilt para injeção de dependência.
 * A anotação @AndroidEntryPoint é necessária para que Hilt possa fornecer as dependências.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura o conteúdo da atividade utilizando Jetpack Compose
        setContent {
            // Aplica o tema definido em PrimeiroProjetoComposeTheme
            PrimeiroProjetoComposeTheme {
                // Obtém uma instância do ViewModelMain usando Hilt para injeção de dependência
                val viewModel: ViewModelMain = hiltViewModel()
                // Chama a função composable PrimeiraTela, passando o ViewModel como parâmetro
                PrimeiraTela(viewModel)
            }
        }
    }

    /**
     * PrimeiraTela é uma função composable que define a interface de usuário principal.
     * @param viewModel A instância do ViewModelMain injetada por Hilt.
     */
    @Composable
    fun PrimeiraTela(viewModel: ViewModelMain = hiltViewModel()) {
        // Estados mutáveis para armazenar os valores dos campos de email e senha
        var email by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }

        // Coluna que organiza os componentes verticalmente
        Column(
            modifier = Modifier.fillMaxSize(), // Preenche todo o espaço disponível
            verticalArrangement = Arrangement.Center, // Centraliza verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
        ) {
            // Campo de texto para o email
            TextField(
                value = email,
                onValueChange = { email = it }, // Atualiza o estado do email
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 50.dp, 20.dp, 10.dp), // Define as margens
                label = { Text(text = "Email") }, // Label do campo de texto
                maxLines = 1 // Limita a uma linha
            )

            // Campo de texto para a senha
            TextField(
                value = senha,
                onValueChange = { senha = it }, // Atualiza o estado da senha
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 20.dp, 10.dp), // Define as margens
                label = { Text(text = "Senha") }, // Label do campo de texto
                maxLines = 1 // Limita a uma linha
            )

            // Botão de login
            Button(
                onClick = {
                    // Chama o método de login do ViewModel
                    viewModel.login(email, senha, object : RespostaServidor {
                        override fun onSucess(mensagem: String) {
                            // Exibe um Toast de sucesso
                            Toast.makeText(this@MainActivity, mensagem, Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(mensagem: String) {
                            // Exibe um Toast de erro
                            Toast.makeText(this@MainActivity, mensagem, Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple500), // Define a cor do botão
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp) // Define as margens
            ) {
                // Texto do botão
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Define a cor do texto
                )
            }
        }
    }

    /**
     * PrimeiraTelaPreview é uma função composable usada para pré-visualizar a tela no Android Studio.
     */
    @Preview
    @Composable
    fun PrimeiraTelaPreview() {
        PrimeiraTela()
    }
}
