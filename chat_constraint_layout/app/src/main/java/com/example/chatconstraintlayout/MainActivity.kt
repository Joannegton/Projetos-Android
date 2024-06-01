package com.example.chatconstraintlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.chatconstraintlayout.item_lista.UsuarioItem
import com.example.chatconstraintlayout.model.Usuario
import com.example.chatconstraintlayout.ui.theme.ChatConstraintLayoutTheme
import com.example.chatconstraintlayout.ui.theme.Green
import com.example.chatconstraintlayout.ui.theme.GreenStatus
import com.example.chatconstraintlayout.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class) //necessário para usar TopAppBar
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(lightScrim = Color.Red.toArgb(), darkScrim = Color.Red.toArgb()))
        setContent {
            ChatConstraintLayoutTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Chat", color = White, fontSize = 25.sp)},
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Green)
                        )
                    },
                    containerColor = White
                ) {
                    ChatApp(Modifier.padding(it))
                }
            }
        }
    }
}

@Composable
fun ChatApp(modifier: Modifier = Modifier) {
    val listaUsuarios = mutableListOf<Usuario>(
        Usuario("Wellington tavares ", R.drawable.homem),
        Usuario("Mãe ", R.drawable.mulher),
        Usuario("Maestro Prof ", R.drawable.homem),
        Usuario("Maria da silva ", R.drawable.mulher),
        Usuario("Wellington tavares ", R.drawable.homem),
        Usuario("Maestro Prof ", R.drawable.homem),
        Usuario("Maria da silva ", R.drawable.mulher),
        Usuario("Wellington tavares ", R.drawable.homem),
        Usuario("Maria da silva ", R.drawable.mulher),
        Usuario("Wellington tavares ", R.drawable.homem),
        Usuario("Maria da silva ", R.drawable.mulher),
        Usuario("Wellington tavares ", R.drawable.homem),
    )

    LazyColumn (Modifier.fillMaxWidth()){
        itemsIndexed(listaUsuarios) { position, _ ->
            UsuarioItem(listaUsuarios, position, context = LocalContext.current)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatConstraintLayoutTheme {
        ChatApp()
    }
}