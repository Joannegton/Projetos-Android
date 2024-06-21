package com.example.diariodememorias.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.tertiaryDark
import com.example.diariodememorias.R
import com.example.diariodememorias.util.criarLivroDeMemorias
import com.example.diariodememorias.data.models.Livro
import com.example.diariodememorias.data.models.Pagina
import com.example.diariodememorias.ui.componentes.VisualizadorImagem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LivroDeMemoriasScreen(navController: NavController) {

    var livro by remember { mutableStateOf(Livro("", listOf())) }
    val pagerState = rememberPagerState(pageCount = { livro.paginas.size })

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        criarLivroDeMemorias { livroCriado ->
            livro = livroCriado
        }

        HorizontalPager(state = pagerState) { page ->
            PaginaCard(livro.paginas[page], navController)
        }
    }

}

@Composable
fun PaginaCard(pagina: Pagina, navController: NavController) {
    var isDialogOpen by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 40.dp)
            ) {
                Text(
                    text = pagina.motivo,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pagina.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Justify,
                    lineHeight = 25.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = pagina.imageUri!!),
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(5.dp, tertiaryDark, MaterialTheme.shapes.medium)
                        .align(Alignment.CenterHorizontally)
                        .clickable { isDialogOpen = true }
                )

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrasta_pro_lado),
                        contentDescription = "Arrasta pro lado",
                        modifier = Modifier
                            .size(50.dp)
                    )

                    if (pagina.motivo == "Motivo 10: Porque você é Você") {
                        Button(
                            onClick = { navController.navigate("diary") },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(text = "Acessar memórias")
                        }

                    }
                }
            }

        }

        VisualizadorImagem(
            imagem = pagina.imageUri!!,
            dialog = isDialogOpen,
            onDialogDismiss = {isDialogOpen = false}
        )

    }
}


