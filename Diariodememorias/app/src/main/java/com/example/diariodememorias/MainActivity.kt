package com.example.diariodememorias

import Login
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.DiarioDeMemoriasTheme
import com.example.diariodememorias.ui.views.ContagemRegressiva
import com.example.diariodememorias.ui.views.DiaryApp
import com.example.diariodememorias.ui.views.LivroDeMemoriasScreen
import com.example.diariodememorias.viewModel.LoginViewModel
import com.example.diariodememorias.viewModel.MemoriaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showDialog by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DiarioDeMemoriasTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Diário de Memórias", fontSize = 27.sp) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = Color.White))
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Black
                ){
                    val navController = rememberNavController()
                    val loginViewModel: LoginViewModel = hiltViewModel()
                    val memoriaVielModel: MemoriaViewModel = hiltViewModel()


                    NavHost(navController = navController, startDestination = "diary", modifier = Modifier.padding(it)) {
                        composable("login") {
                            Login(loginViewModel, onLoginSuccess = {
                                navController.navigate("contagemRegressiva")
                            })
                        }
                        composable("diary") {
                            DiaryApp(
                                memoriaVielModel,
                                showDialog = showDialog, // Passa o estado
                                onOpenViewer = { showDialog = true }, // Função para abrir o visualizador
                                onCloseViewer = { showDialog = false } // Função para fechar o visualizador
                            )
                        }
                        composable("livro10motivos") { LivroDeMemoriasScreen(navController) }
                        composable("contagemRegressiva") { ContagemRegressiva(navController) }
                    }
                }


            }

        }

        // Configura o callback para o botão de voltar
        onBackPressedDispatcher.addCallback(this){
            if (showDialog) {
                showDialog = false // Fecha o visualizador
            } else {
                finish() // Finaliza a Activity se o visualizador não estiver visível
            }
        }
    }


}

