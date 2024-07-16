package com.marcos.appdecafeteria.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marcos.appdecafeteria.itemlista.ProdutoItem
import com.marcos.appdecafeteria.model.Produto
import com.marcos.appdecafeteria.ui.theme.BEIGE
import com.marcos.appdecafeteria.ui.theme.BLACK
import com.marcos.appdecafeteria.ui.theme.CHOCOLATE_COLOR
import com.marcos.appdecafeteria.ui.theme.GREEN
import com.marcos.appdecafeteria.ui.theme.PINK
import com.marcos.appdecafeteria.ui.theme.PISTACHECOLOR
import com.marcos.appdecafeteria.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController
){

    val listaProdutos: MutableList<Produto> = mutableListOf(
        Produto(
            imagem = Icons.Filled.Coffee,
            iconColor = BEIGE,
            nome = "Cappuccino de Baunilha",
            preco = "15.00"
        ),
        Produto(
            imagem = Icons.Filled.Coffee,
            iconColor = CHOCOLATE_COLOR,
            nome = "Cappuccino de Chocolate",
            preco = "18.90"
        ),
        Produto(
            imagem = Icons.Filled.Coffee,
            iconColor = PISTACHECOLOR,
            nome = "Cappuccino de Pistache",
            preco = "22.99"
        ),
        Produto(
            imagem = Icons.Filled.Fastfood,
            iconColor = GREEN,
            nome = "Hambúrguer + Refrigerante ( Combo )",
            preco = "24.90"
        ),
        Produto(
            imagem = Icons.Filled.Icecream,
            iconColor = CHOCOLATE_COLOR,
            nome = "Sorvete de Chocolate",
            preco = "4.00"
        ),
        Produto(
            imagem = Icons.Filled.Cake,
            iconColor = PINK,
            nome = "Bolo de Morango",
            preco = "30.90"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                actions = {

                    Text(
                        text = "Pedidos",
                        fontSize = 18.sp,
                        color = WHITE,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(10.dp, 0.dp)
                            .clickable {
                                navController.navigate("Pedidos")
                            }
                    )

                    Icon(
                        imageVector = Icons.Filled.Checklist,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = WHITE
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
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Lista de Produtos",
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(20.dp)
            )

            LazyColumn {
                itemsIndexed(listaProdutos){position,_ ->
                    ProdutoItem(listaProdutos = listaProdutos, position = position, navController = navController)
                }
            }
        }

    }
}