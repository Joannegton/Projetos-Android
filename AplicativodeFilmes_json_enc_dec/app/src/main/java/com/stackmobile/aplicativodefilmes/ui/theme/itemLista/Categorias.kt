package com.stackmobile.aplicativodefilmes.ui.theme.itemLista


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.stackmobile.aplicativodefilmes.R
import com.stackmobile.aplicativodefilmes.ui.theme.WHITE
import com.stackmobile.aplicativodefilmes.ui.theme.model.Categoria
import java.net.URLEncoder

@Composable
fun Categorias(
    categorias: MutableList<Categoria>,
    position: Int,
    navController: NavController
){
    val titulo = categorias[position].titulo
    val filmes = categorias[position].filmes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = titulo!!, color = WHITE)

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(filmes){position,_ ->

                       AsyncImage(
                           model = filmes[position].capa,
                           contentDescription = null,
                           modifier = Modifier
                               .width(120.dp)
                               .height(130.dp)
                               .padding(5.dp)
                               .clickable(
                                   onClick = {

                                       val id = filmes[position].id
                                       val nome = filmes[position].nome
                                       val url = filmes[position].capa
                                       val descricao = filmes[position].descricao
                                       val elenco = filmes[position].elenco

                                       navController.navigate(
                                           "DetalhesFilme/$id/$nome/${URLEncoder.encode(url,"UTF-8")}/$descricao/$elenco"
                                       )
                                   },
                                   role = Role.Image
                               ),
                           contentScale = ContentScale.FillBounds,
                           clipToBounds = true,
                           placeholder = painterResource(id = R.drawable.placeholder_image_film)
                       )


            }
        }
    }

}