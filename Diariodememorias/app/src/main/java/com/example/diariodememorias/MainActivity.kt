package com.example.diariodememorias

import Login
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.DiarioDeMemoriasTheme
import com.example.diariodememorias.ui.componentes.TopAppBarMaster
import com.example.diariodememorias.ui.views.Cadastro
import com.example.diariodememorias.ui.views.ContagemRegressiva
import com.example.diariodememorias.ui.views.DiaryApp
import com.example.diariodememorias.ui.views.LivroDeMemoriasScreen
import com.example.diariodememorias.viewModel.ConexaoViewModel
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel
import com.example.diariodememorias.viewModel.LoginViewModel
import com.example.diariodememorias.viewModel.MemoriaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DiarioDeMemoriasTheme {
                val navController = rememberNavController()

                val conexaoViewModel: ConexaoViewModel = hiltViewModel()
                val gerenciadorViewModel: GerenciamentoSessaoViewModel = hiltViewModel()
                val loginViewModel: LoginViewModel = hiltViewModel()
                val memoriaVielModel: MemoriaViewModel = hiltViewModel()

                Scaffold(
                    topBar = { TopAppBarMaster(navController, conexaoViewModel, gerenciadorViewModel) },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Black
                ){


                    NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(it)) {
                        composable("login") { Login(navController,loginViewModel, onLoginSuccess = {navController.navigate("diary")})}
                        composable("cadastro"){ Cadastro(navController = navController, viewModel = loginViewModel)}
                        composable("diary") {
                            DiaryApp(
                                memoriaVielModel,
                                gerenciadorViewModel,
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
