package com.example.contatosroomdatabaseenavegao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.contatosroomdatabaseenavegao.ui.theme.ContatosRoomDatabaseENavegaçãoTheme
import com.example.contatosroomdatabaseenavegao.views.AtualizarContato
import com.example.contatosroomdatabaseenavegao.views.ListaContatos
import com.example.contatosroomdatabaseenavegao.views.SalvarContato

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContatosRoomDatabaseENavegaçãoTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "listaContatos") {
                    composable("listaContatos") { ListaContatos(navController) }
                    composable("salvarContato") { SalvarContato(navController) }
                    composable(
                        "atualizaContato/{uid}",
                        arguments = listOf(navArgument("uid"){})
                    ) {
                        AtualizarContato(navController, it.arguments?.getString("uid").toString())
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContatosRoomDatabaseENavegaçãoTheme {

    }
}