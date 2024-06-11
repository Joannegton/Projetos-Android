package com.example.diariodememorias

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.diariodememorias.funcoes.adicionarMemoria
import com.example.diariodememorias.funcoes.conectarparceiro
import com.example.diariodememorias.funcoes.enviarMidia
import com.example.diariodememorias.funcoes.fetchMemories
import com.example.diariodememorias.funcoes.pegarMemorias
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryApp() {
    val memories = remember { mutableStateListOf<Memoria>() }
    var showAddMemoryDialog by remember { mutableStateOf(false) }

    val usuarioId = Firebase.auth.currentUser?.uid
    var parceiroId: String = "89vwmTeizAYcULg7JBWMG4aArNc2"

    conectarparceiro(usuarioId, parceiroId) { sucesso, mensagem ->
        if (sucesso) {
            Log.i("TAG", "Conectado com sucesso")
        } else {
            Log.e("TAG", "Erro ao conectar: $mensagem")
        }
    }

    if (usuarioId != null) {
        val userRef = Firebase.firestore.collection("usuarios").document(usuarioId)
        userRef.get().addOnSuccessListener { document ->
            if (document != null) {
                parceiroId = document.getString("parceiroId").toString()
            } else {
                Log.d("TAG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("TAG", "get failed with ", exception)
        }
    }

    LaunchedEffect(Unit) {
        launch {
            val fetchedMemories = fetchMemories(usuarioId.toString(), parceiroId)
            memories.clear()
            memories.addAll(fetchedMemories)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Diário de Memórias") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddMemoryDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Memória")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            ListaMemoriaScreen(usuarioId.toString(), parceiroId)
            if (showAddMemoryDialog) {
                AddMemoriaScreen(usuarioId.toString(), parceiroId, showAddMemoryDialog)
            }
        }

    }
}

@Composable
fun ListaMemoriaScreen(usuarioId: String, parceiroId: String?) {
    var memorias by remember { mutableStateOf(listOf<Memoria>()) }

    LaunchedEffect(Unit) {
        pegarMemorias(usuarioId, parceiroId.toString()) { listaMemorias ->
            memorias = listaMemorias
        }
    }
    LazyColumn {
        items(memorias) { memory ->
            MemoryCard(memory)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MemoryCard(memory: Memoria) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = memory.title, style = MaterialTheme.typography.titleLarge)
            Text(text = memory.description, style = MaterialTheme.typography.bodyMedium)
            memory.imageUri?.let {
                Image(
                    painter = rememberImagePainter(memory.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun AddMemoriaScreen(usuarioId: String, parceiroId: String?, showDialog: Boolean) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(showDialog) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagemUri = uri
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text("Adicionar Memória") },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") })
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição") })
                    // Seleção de imagem e upload
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") }
                    ) {
                        Text("Selecionar Imagem")
                    }

                    imagemUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty() && imagemUri != null) {
                        enviarMidia(imagemUri!!) { downloadUrl ->
                            if (downloadUrl != "null") {
                                val memory =
                                    Memoria(title, description, downloadUrl, usuarioId, parceiroId)
                                adicionarMemoria(memory, usuarioId, parceiroId) { sucesso, mensagem ->
                                    if (sucesso) {
                                        Log.i("TAG", "Memória adicionada com sucesso")
                                        showDialog = false
                                    } else {
                                        Log.e("TAG", "Erro ao adicionar a memória: $mensagem")
                                    }
                                }
                                Log.i("TAG", "Imagem enviada com sucesso")
                            } else {
                                Log.e("TAG", "Erro ao enviar a imagem")
                            }
                        }
                    } else {
                        Log.i("TAG", "Erro ao adicionar a memória")
                    }
                }) {
                    Text(text = "Adicionar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}



