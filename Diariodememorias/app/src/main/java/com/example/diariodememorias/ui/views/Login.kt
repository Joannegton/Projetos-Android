import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.backgoundContainer
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight
import com.example.diariodememorias.R
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.viewModel.LoginViewModel
import com.example.diariodememorias.viewModel.ResultadoLogin

// Função Composable para a tela de Login
@Composable
fun Login(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    // Estados mutáveis para os campos de entrada de email e senha
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Obtenha o contexto atual para exibir mensagens Toast
    val context = LocalContext.current
    // Observe o estado de login do ViewModel
    val loginState by viewModel.loginState.collectAsState()

    // Layout para a tela de Login
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(backgoundContainer)
    ) {
        // Exibição de imagem
        Image(
            painter = painterResource(R.drawable.eueela), contentDescription = "nós",
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .border(5.dp, secondaryLight, MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        // Texto de dica
        Text(
            text = "Realize seu Login",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(5.dp))

        // Campo de entrada de email
        EntradaTexto(
            texto = email,
            onValueChange = {email = it},
            label = "email",
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de senha
        EntradaTexto(
            texto = senha,
            onValueChange = {senha = it},
            label = "Senha",
            isSenha = true,
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.padding(10.dp))

        // Botão de Login
        Botao(
            onClick = {
            // Chama a função de login no ViewModel quando o botão for clicado
            viewModel.entrar(email, senha)
            },
            texto = "Entrar"
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row (horizontalArrangement = Arrangement.Center ,modifier = Modifier.fillMaxWidth()){
            Text(text = "Não possui conta?", fontSize = 18.sp)
            Text(
                text = "Cadastre-se",
                color = secondaryLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp)
            )
        }

    }
    // Observe o estado de login e reaja às mudanças
    LaunchedEffect(loginState?.sucesso) { // A chave muda apenas quando o sucesso muda
        if (loginState is ResultadoLogin) {
            if (loginState!!.sucesso) {
                onLoginSuccess()
            } else{
                Log.i("Tag", loginState!!.msg!!)
                Toast.makeText(context, loginState!!.msg, Toast.LENGTH_SHORT).show()
            }
            viewModel.resetEstadoLogin()
        }
    }
}

@Preview
@Composable
private fun View() {
    val viewModel: LoginViewModel = hiltViewModel()
    Login(viewModel = viewModel, onLoginSuccess = {})


}