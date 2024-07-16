package com.stackmobile.aplicativodefilmes.ui.theme.view


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.stackmobile.aplicativodefilmes.ui.theme.BLACK100
import com.stackmobile.aplicativodefilmes.ui.theme.BLACK50
import com.stackmobile.aplicativodefilmes.ui.theme.WHITE
import com.stackmobile.aplicativodefilmes.ui.theme.itemLista.Categorias
import com.stackmobile.aplicativodefilmes.ui.theme.model.Categoria
import com.stackmobile.aplicativodefilmes.ui.theme.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
){

    var progressBarVisibility by remember { mutableStateOf(true)}
    var pontosAnimados by remember { mutableIntStateOf(0) }
    var internetStatus by remember { mutableStateOf(false)}

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var listaCategorias by remember { mutableStateOf<MutableList<Categoria>> (mutableListOf()) }

    LaunchedEffect(Unit) {

        val isConnected = checkInternetConnection(context)

        scope.launch(Dispatchers.IO){

            if (!isConnected){
                internetStatus = true
            }else{
                viewModel.getCategorias(respostaDoServidor = {categorias ->
                    listaCategorias = categorias
                    progressBarVisibility = false
                }, mensagemErro = {mensagemErro ->
                    Toast.makeText(context,mensagemErro,Toast.LENGTH_SHORT).show()
                })
            }

        }

        scope.launch(Dispatchers.Main){
            if (internetStatus){
                Toast.makeText(context,"Erro de acesso a internet!",Toast.LENGTH_SHORT).show()
            }
        }

        while (true){
            delay(500)
            pontosAnimados = (pontosAnimados + 1) % 4
        }


    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Filmes",
                        fontSize = 18.sp,
                        color = WHITE
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BLACK100
                )
            )
        },
    ) {paddingValues ->

            if (progressBarVisibility){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(BLACK50),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color = Color.Red,
                        modifier = Modifier.size(50.dp),
                        strokeWidth = 5.dp
                    )

                    Text(
                        text = "Carregando"+ ".".repeat(pontosAnimados),
                        color = WHITE,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(BLACK50)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        itemsIndexed(listaCategorias) { position, _ ->
                            Categorias(listaCategorias, position,navController)
                        }
                    }
                }
            }

    }

}

fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected ?: false
    }
}


