package com.example.diariodememorias.views

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.compose.DiarioDeMemoriasTheme
import com.example.compose.backgoundContainer
import com.example.compose.primaryContainerDark
import com.example.compose.primaryContainerLight
import com.example.compose.primaryContainerLightMediumContrast
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight
import com.example.compose.tertiaryDark
import com.example.compose.tertiaryLight
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.funcoes.adicionarMemoria
import com.example.diariodememorias.funcoes.conectarparceiro
import com.example.diariodememorias.funcoes.enviarMidia
import com.example.diariodememorias.funcoes.fetchMemories
import com.example.diariodememorias.funcoes.pegarMemorias
import com.example.diariodememorias.ui.componentes.VisualizadorImagem
import com.example.diariodememorias.ui.componentes.VisualizadorImagemUrl
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryApp() {
    val memories = remember { mutableStateListOf<Memoria>() }
    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var memorias by remember { mutableStateOf(listOf<Memoria>()) }


    val usuarioId = Firebase.auth.currentUser?.uid
    var parceiroId = "TFBncpRirNRNK2L1Q84KschgPED3"

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
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddMemoryDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Memória")
            }
        }
    ) {
        Box {
            val imagePainter = rememberImagePainter("https://example.com/your-image.jpg")
            var imagem by remember { mutableStateOf(imagePainter) }
            var showDialog by remember { mutableStateOf(false) }

            Column(Modifier.fillMaxSize()) {
                LaunchedEffect(Unit) {
                    pegarMemorias(usuarioId.toString(), parceiroId) { listaMemorias ->
                        memorias = listaMemorias
                    }
                }




                LazyColumn() {
                    items(memorias) { memory ->
                        MemoryCard(memory) { img, show ->
                            imagem = img
                            showDialog = show
                        }
                    }
                }
                if (showAddMemoryDialog) {
                    AddMemoriaScreen(usuarioId.toString(), parceiroId, showAddMemoryDialog)
                }
            }
            VisualizadorImagemUrl(
                imagePainter = imagem,
                dialog = showDialog,
                onDialogDismiss = { showDialog = false }
            )
        }

    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MemoryCard(memory: Memoria, onImagePainter: (ImagePainter, Boolean) -> Unit) {
    var showImageDialog by remember { mutableStateOf(false) }
    val imagem = rememberImagePainter(memory.imageUri)

    Box(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .zIndex(0f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = memory.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Justify,
                    lineHeight = 25.sp
                )

                //Text(text = "Ver mais", modifier = Modifier.fillMaxWidth().padding(end = 16.dp), textAlign = TextAlign.End)

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = imagem,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImagePainter(imagem, true); showImageDialog = true }

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
            onDismissRequest = { },
            title = { Text("Adicionar Memória") },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = tertiaryDark,
                            unfocusedContainerColor = tertiaryDark,
                            focusedTextColor = secondaryLight,
                            unfocusedTextColor = secondaryLight,
                            cursorColor = secondaryLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = secondaryLight,
                            focusedLabelColor = secondaryLight,
                            unfocusedLabelColor = secondaryLight,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = tertiaryDark,
                            unfocusedContainerColor = tertiaryDark,
                            focusedTextColor = secondaryLight,
                            unfocusedTextColor = secondaryLight,
                            cursorColor = secondaryLight,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = secondaryLight,
                            focusedLabelColor = secondaryLight,
                            unfocusedLabelColor = secondaryLight,
                        )
                    )
                    // Seleção de imagem e upload
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tertiaryDark,
                            contentColor = secondaryLight
                        )
                    ) {
                        Text("Selecionar Imagem", fontWeight = FontWeight.Bold)
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
                Button(
                    onClick = {
                        if (title.isNotEmpty() && description.isNotEmpty() && imagemUri != null) {
                            enviarMidia(imagemUri!!) { downloadUrl ->
                                if (downloadUrl != "null") {
                                    val memory =
                                        Memoria(
                                            title,
                                            description,
                                            downloadUrl,
                                            usuarioId,
                                            parceiroId
                                        )
                                    adicionarMemoria(
                                        memory,
                                        usuarioId,
                                        parceiroId
                                    ) { sucesso, mensagem ->
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tertiaryDark,
                        contentColor = secondaryLight
                    )
                ) {
                    Text(text = "Adicionar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tertiaryDark,
                        contentColor = secondaryLight
                    )
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = primaryContainerLight,
            titleContentColor = tertiaryLight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetinPreview() {
    DiarioDeMemoriasTheme {
        DiaryApp()
    }
}


