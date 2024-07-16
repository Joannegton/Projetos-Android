package com.marcos.appdecafeteria

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcos.appdecafeteria.view.Home
import com.marcos.appdecafeteria.view.Pagamento
import com.marcos.appdecafeteria.view.Pedidos

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            NavHost(
                startDestination = "Home",
                navController = navController
            ){
                composable(
                    route = "Home"
                ){
                    Home(navController = navController)
                }

                composable(
                    route = "Pagamento/{initPoint}/{produto}/{preco}",
                    arguments = listOf(
                        navArgument("initPoint"){type = NavType.StringType},
                        navArgument("produto"){type = NavType.StringType},
                        navArgument("preco"){type = NavType.StringType}
                    )
                ){
                    Pagamento(
                        produto = it.arguments?.getString("produto"),
                        preco = it.arguments?.getString("preco"),
                        initPoint = it.arguments?.getString("initPoint"),
                        navController = navController
                    )
                }

                composable(
                    route = "Pedidos"
                ){
                    Pedidos(navController = navController)
                }

            }
        }
    }
}
