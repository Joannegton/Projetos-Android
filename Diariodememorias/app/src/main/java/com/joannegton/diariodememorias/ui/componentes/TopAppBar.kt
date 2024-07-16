package com.joannegton.diariodememorias.ui.componentes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import com.joannegton.diariodememorias.viewModel.ConexaoState
import com.joannegton.diariodememorias.viewModel.ConexaoViewModel
import com.joannegton.diariodememorias.viewModel.GerenciamentoSessaoViewModel

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
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = mostrarMenu,
                onDismissRequest = { mostrarMenu = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Sair", fontSize = 16.sp) },
                    onClick = {
                        viewModel2.sair()
                        navController.navigate("login")
                        mostrarMenu = false
                    },
                    leadingIcon = {Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")},
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        leadingIconColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Divider(
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )

                val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){}
                DropdownMenuItem(
                    text = { Text(text = "Reportar Bug/Melhoria", fontSize = 16.sp) },
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/e7oyHvGrUjmbai9M6"))
                        launcher.launch(intent)
                        mostrarMenu = false
                    },
                    leadingIcon = {Icon(Icons.Filled.Adb, contentDescription = "Bug")},
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        leadingIconColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Divider(
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }



        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
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
