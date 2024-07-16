package com.stackmobile.aplicativodefilmes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stackmobile.aplicativodefilmes.ui.theme.view.DetalhesFilme
import com.stackmobile.aplicativodefilmes.ui.theme.view.Home
import com.stackmobile.aplicativodefilmes.ui.theme.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val homeViewModel: HomeViewModel = hiltViewModel()

            NavHost(
                navController = navController,
                startDestination = "Home"
            ){

                composable(
                    route = "Home"
                ){
                    Home(navController = navController, viewModel = homeViewModel)
                }

                composable(
                    route = "DetalhesFilme/{id}/{nome}/{url}/{descricao}/{elenco}",
                    arguments = listOf(
                        navArgument(
                            name = "id", builder = { type = NavType.IntType },
                        ),
                        navArgument(
                            name = "nome", builder = { type = NavType.StringType },
                        ),
                        navArgument(
                            name = "url", builder = { type = NavType.StringType },
                        ),
                        navArgument(
                            name = "descricao", builder = { type = NavType.StringType },
                        ),
                        navArgument(
                            name = "elenco", builder = { type = NavType.StringType },
                        )
                    )
                ){
                    DetalhesFilme(
                        navController = navController,
                        id = it.arguments?.getInt("id"),
                        nome = it.arguments?.getString("nome"),
                        url = it.arguments?.getString("url"),
                        descricao = it.arguments?.getString("descricao"),
                        elenco = it.arguments?.getString("elenco")
                    )
                }

            }
        }
    }
}
