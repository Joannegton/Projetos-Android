package com.example.diariodememorias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diariodememorias.ui.componentes.TopAppBarMaster
import com.example.diariodememorias.ui.theme.DiarioDeMemoriasTheme
import com.example.diariodememorias.ui.views.Cadastro
import com.example.diariodememorias.ui.views.DiaryApp
import com.example.diariodememorias.ui.views.Login
import com.example.diariodememorias.viewModel.ConexaoViewModel
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel
import com.example.diariodememorias.viewModel.MemoriaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

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
                val memoriaVielModel: MemoriaViewModel = hiltViewModel()

                val usuarioLogado by gerenciadorViewModel.usuarioLogado()
                    .collectAsState(initial = false)

                Scaffold(
                    topBar = {
                        if (usuarioLogado) {
                            TopAppBarMaster(navController, conexaoViewModel, gerenciadorViewModel)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.Black
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(it)
                    ) {
                        composable("splash") {
                            SplashScreen(onTimeout = {
                                navController.navigate("login"){
                                    popUpTo("splash"){
                                        inclusive = true
                                    }
                                }
                            })
                        }
                        composable("login") {
                            Login(
                                navController,
                                gerenciadorViewModel,
                                memoriaVielModel,
                                onLoginSuccess = { navController.navigate("diary") }
                            )
                        }
                        composable("cadastro") {
                            Cadastro(
                                navController = navController,
                                viewModel = gerenciadorViewModel
                            )
                        }
                        composable("diary") {
                            DiaryApp(
                                memoriaVielModel,
                                gerenciadorViewModel,
                                showDialog = showDialog, // Passa o estado
                                onOpenViewer = {
                                    showDialog = true
                                }, // Função para abrir o visualizador
                                onCloseViewer = {
                                    showDialog = false
                                } // Função para fechar o visualizador
                            )
                        }
                    }
                }


            }

        }

        // Configura o callback para o botão de voltar
        onBackPressedDispatcher.addCallback(this) {
            if (showDialog) {
                showDialog = false // Fecha o visualizador
            } else {
                finish() // Finaliza a Activity se o visualizador não estiver visível
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Ou a cor de fundo desejadacontentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.report_bug), // Substitua pela sua imagem
            contentDescription = "ReportBug",
            modifier = Modifier.fillMaxWidth() // Ajuste o tamanho conforme necessário
        )
    }

    // Inicia um timer para chamar onTimeout após 5 segundos
    LaunchedEffect(key1 = true) {
        delay(5000)
        onTimeout()
    }
}
