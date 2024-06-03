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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimeiroProjetoComposeTheme {
                val viewModel: ViewModelMain = hiltViewModel()
                PrimeiraTela(viewModel)
            }
        }
    }

   @Composable
   fun PrimeiraTela(viewModel: ViewModelMain = hiltViewModel()){

       var email by remember {
           mutableStateOf("")
       }

       var senha by remember {
           mutableStateOf("")
       }

      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {


          TextField(
              value = email,
              onValueChange = {
                  email = it
              },
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp, 50.dp, 20.dp, 10.dp),
              label = {
                  Text(text = "Email")
              },
              maxLines = 1
          )

          TextField(
              value = senha,
              onValueChange = {
                  senha = it
              },
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp, 0.dp, 20.dp, 10.dp),
              label = {
                  Text(text = "Senha")
              },
              maxLines = 1
          )

          Button(
              onClick = {
                    viewModel.login(email, senha, object: RespostaServidor{
                        override fun onSucess(mensagem: String) {
                            Toast.makeText(this@MainActivity, mensagem, Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(mensagem: String) {
                            Toast.makeText(this@MainActivity, mensagem, Toast.LENGTH_SHORT).show()

                        }

                    })
              },
              colors = ButtonDefaults.buttonColors(
                  backgroundColor = Purple500
              ),
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp)
          ) {
              Text(text = "Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }

      }
   }

   @Preview
   @Composable
   fun PrimeiraTelaPreview(){
       PrimeiraTela()
   }
}
