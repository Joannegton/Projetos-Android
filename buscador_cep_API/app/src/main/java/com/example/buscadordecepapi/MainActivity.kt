package com.example.buscadordecepapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.buscadordecepapi.ui.theme.BuscadorDeCepAPITheme
import com.example.buscadordecepapi.view.BuscadorCep
import com.example.buscadordecepapi.view.DetalhesEndereco
import com.example.buscadordecepapi.viewModel.BuscarCepViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuscadorDeCepAPITheme {
                val navController = rememberNavController()
                val viewModel: BuscarCepViewModel = hiltViewModel()
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Buscador de CEP", fontSize = 20.sp, color = Color.White) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1AB484)
                            )
                        )
                    }
                ){
                    NavHost(navController = navController, startDestination = "buscadorCep", Modifier.padding(it)) {
                        composable(
                            "buscadorCep"
                        ) {
                            BuscadorCep(navController, viewModel)
                        }
                        composable(
                            "detalhesEndereco/{logradouro}/{bairro}/{cidade}/{estado}",
                            arguments = listOf(
                                navArgument("logradouro"){type = NavType.StringType},
                                navArgument("bairro"){type = NavType.StringType},
                                navArgument("cidade"){type = NavType.StringType},
                                navArgument("estado"){type = NavType.StringType},
                            )
                        ){
                            DetalhesEndereco(
                                navController = navController,
                                it.arguments?.getString("logradouro").toString(),
                                it.arguments?.getString("bairro").toString(),
                                it.arguments?.getString("cidade").toString(),
                                it.arguments?.getString("estado").toString(),
                            )
                        }
                    }

                }
            }
        }
    }
}
