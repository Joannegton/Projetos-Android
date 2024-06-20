package com.example.diariodememorias.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diariodememorias.viewModel.ConexaoState
import com.example.diariodememorias.viewModel.ConexaoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMaster(viewModel: ConexaoViewModel) {
    var isParceiro by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }
    val conexaoState by viewModel.conexaoState.collectAsState()

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text("Diário de Memórias", fontSize = 27.sp)

                if (!isParceiro){
                    IconButton(
                        onClick = {
                            mostrarDialog = true
                        }
                    ) {
                        IconeConectar()
                    }
                    if (mostrarDialog){
                        ConectarParceiroDialog(
                            onConfirm = {email -> viewModel.conectarParceiro("",email)},
                            onDismiss = { mostrarDialog = false }
                        )
                    }

                }
                when(conexaoState){
                    ConexaoState.Idle -> {}
                    ConexaoState.Loading -> {}
                    ConexaoState.Success -> {
                        isParceiro = true
                    }
                    is ConexaoState.Error -> {
                        val mensagemErro = (conexaoState as ConexaoState.Error).mensagem
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun IconeConectar(modifier: Modifier = Modifier) {
    Box{
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Search",
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.Center)
                .size(35.dp)
        )
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Plus",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Plus",
            tint = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}
