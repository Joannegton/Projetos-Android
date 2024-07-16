package com.marcos.appdecafeteria.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marcos.appdecafeteria.datasource.FirebaseSdk
import com.marcos.appdecafeteria.itemlista.PedidoItem
import com.marcos.appdecafeteria.model.Pedido
import com.marcos.appdecafeteria.ui.theme.BEIGE
import com.marcos.appdecafeteria.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pedidos(
    navController: NavController
){

    var listaPedidos by remember {mutableStateOf(mutableListOf<Pedido>())}
    val firebaseSdk = FirebaseSdk()

    LaunchedEffect(true){
        firebaseSdk.getPedidos {listaPedidosServidor ->
            listaPedidos = listaPedidosServidor
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                  Text(
                      text = "Pedidos",
                      fontSize = 18.sp,
                      color = WHITE
                  )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BEIGE
                )
            )
        }
    ) {paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.LightGray)
        ) {

            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                itemsIndexed(listaPedidos){position,_ ->
                    PedidoItem(listaPedidos = listaPedidos, position = position)
                }
            }
        }

    }
}