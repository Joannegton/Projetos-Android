package com.example.diariodememorias.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.secondaryLight
import com.example.diariodememorias.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ContagemRegressiva(navController: NavController) {
    var timeLeft by remember { mutableIntStateOf(11) }
    var displayedImages by remember { mutableStateOf(listOf<Int>()) }

    val images = listOf(
        R.drawable.baladinha_top1,
        R.drawable.beijinho_lado,
        R.drawable.beijinho_rosto_motel,
        R.drawable.cabelo_liso_espelho,
        R.drawable.eueela,
        R.drawable.goticos2,
        R.drawable.maozinhadada_carro,
        R.drawable.motel_luz_vermelha,
        R.drawable.role_reggae,
        R.drawable.gothic_love,
    )

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            displayedImages = images.take(11 - timeLeft)
            delay(1000)
            timeLeft -= 1
        }
        Log.d("TAG", "Navegando para livro10motivos")

        navController.navigate("livro10motivos")

    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("10 motivos por que eu amo você!", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = timeLeft.toString(),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        displayedImages.forEachIndexed { index, image ->
            val imageWidth = 200.dp
            val imageHeight = 200.dp
            val offsetX = remember { mutableStateOf(Random.nextFloat() * (this.maxWidth - imageWidth)) }
            val offsetY = remember { mutableStateOf(Random.nextFloat() * (this.maxHeight - imageHeight)) }

            Image(
                painter = painterResource(id = image),
                contentDescription = "Image for countdown",
                modifier = if (index == displayedImages.lastIndex) {
                    Modifier
                        .size(imageWidth, imageHeight)
                        .align(Alignment.Center)
                        .clip(MaterialTheme.shapes.medium)
                        .border(5.dp, secondaryLight, MaterialTheme.shapes.medium)
                } else {
                    Modifier
                        .size(imageWidth, imageHeight)
                        .offset(x = offsetX.value.value.dp, y = offsetY.value.value.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(5.dp, secondaryLight, MaterialTheme.shapes.medium)
                }
            )
        }
    }
}


