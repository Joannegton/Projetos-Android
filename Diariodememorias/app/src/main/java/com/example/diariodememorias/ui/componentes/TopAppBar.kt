package com.example.diariodememorias.ui.componentes

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diariodememorias.viewModel.ConexaoState
import com.example.diariodememorias.viewModel.ConexaoViewModel
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMaster(
    navController: NavController,
    viewModel: ConexaoViewModel,
    viewModel2: GerenciamentoSessaoViewModel,
) {
    var mostrarDialog by remember { mutableStateOf(false) }
    val conexaoState by viewModel.conexaoState.collectAsState()
    var mostrarMenu by remember { mutableStateOf(false) }
    var isParceiro by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val parceiroId = viewModel2.parceiroId()
    if (parceiroId != null) {
        isParceiro = true
    }


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

                if (!isParceiro) {
                    IconButton(
                        onClick = {
                            mostrarDialog = true
                        }
                    ) {
                        IconeConectar()
                    }
                    if (mostrarDialog) {
                        val usuarioId = viewModel2.usuarioId()
                        ConectarParceiroDialog(
                            onConfirm = { email ->
                                viewModel.conectarParceiro(usuarioId, email)
                            },
                            onDismiss = { mostrarDialog = false }
                        )
                    }
                }

                when (conexaoState) {
                    ConexaoState.Idle -> {}
                    ConexaoState.Loading -> {}
                    ConexaoState.Success -> {
                        isParceiro = true
                        Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show()
                    }

                    is ConexaoState.Error -> {
                        Toast.makeText(context, "Email não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        },
        actions = {
            // Ícone de três pontinhos
            IconButton(onClick = { mostrarMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            DropdownMenu(
                expanded = mostrarMenu,
                onDismissRequest = { mostrarMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Sair") },
                    onClick = {
                        viewModel2.sair()
                        navController.navigate("login")
                        mostrarMenu = false
                    }
                )
            }



        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@Composable
fun IconeConectar() {
    Box {
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
