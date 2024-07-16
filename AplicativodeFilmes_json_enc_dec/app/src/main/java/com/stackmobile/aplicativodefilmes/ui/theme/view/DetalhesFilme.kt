package com.stackmobile.aplicativodefilmes.ui.theme.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stackmobile.aplicativodefilmes.ui.theme.BLACK100
import com.stackmobile.aplicativodefilmes.ui.theme.BLACK50
import com.stackmobile.aplicativodefilmes.ui.theme.WHITE
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesFilme(
    navController: NavController,
    id: Int?,
    nome: String?,
    url: String?,
    descricao: String?,
    elenco: String?
){

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detalhes do Filme", color = WHITE, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BLACK100
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = WHITE
                        )
                    }
                }
            )


        }
    ) {paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BLACK50)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = URLDecoder.decode(url,"UTF-8"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .graphicsLayer {
                        shadowElevation = 500f
                        ambientShadowColor = BLACK100
                    },
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = nome!!,
                fontSize = 18.sp,
                color = WHITE,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )

            Text(
                text = descricao!!,
                fontSize = 14.sp,
                color = WHITE,
                modifier = Modifier.padding(20.dp,10.dp,20.dp,0.dp)
            )
            Text(
                text = "Elenco: $elenco",
                fontSize = 14.sp,
                color = WHITE,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )

            Button(
                onClick = {
                   Toast.makeText(context,"Id: $id Nome: $nome",Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BLACK50,
                    contentColor = WHITE
                ),
                modifier = Modifier.fillMaxWidth().padding(80.dp,20.dp).border(
                    width = 2.dp,
                    color = WHITE,
                    shape = RoundedCornerShape(30.dp)
                )
            ) {
                Text(
                    text = "Assistir",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }


}