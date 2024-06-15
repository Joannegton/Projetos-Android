package com.example.diariodememorias.ui.views

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.compose.primaryContainerLight
import com.example.compose.secondaryLight
import com.example.compose.tertiaryDark
import com.example.compose.tertiaryLight
import com.example.diariodememorias.funcoes.adicionarMemoria
import com.example.diariodememorias.funcoes.conectarparceiro
import com.example.diariodememorias.funcoes.enviarMidia
import com.example.diariodememorias.funcoes.fetchMemories
import com.example.diariodememorias.funcoes.pegarMemorias
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.ui.componentes.VisualizadorImagemUrl
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

// Função principal do aplicativo de diário de memórias
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryApp() {
    val memories = remember { mutableStateListOf<Memoria>() }
    var showAddMemoryDialog by remember { mutableStateOf(false) }
    var update by remember { mutableStateOf(false) } // Variável de controle de atualização

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
                Log.d("TAG", "Documento não encontrado")
            }
        }.addOnFailureListener { exception ->
            Log.d("TAG", "Erro ao recuperar documento: ", exception)
        }
    }

    // Função para atualizar a lista de memórias
    suspend fun updateMemories() {
        val fetchedMemories = fetchMemories(usuarioId.toString(), parceiroId)
        memories.clear()
        memories.addAll(fetchedMemories)
    }

    // Chama updateMemories quando 'update' mudar para true
    LaunchedEffect(update) {
        if (update) {
            updateMemories()
            update = false
        }
    }

    LaunchedEffect(Unit) {
        updateMemories()
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
                LazyColumn() {
                    items(memories) { memory ->
                        CardMemoria(memory) { img, show ->
                            imagem = img
                            showDialog = show
                        }
                    }
                }
                if (showAddMemoryDialog) {
                    AddMemoriaScreen(
                        usuarioId = usuarioId.toString(),
                        parceiroId = parceiroId,
                        showDialog = showAddMemoryDialog,
                        onDismiss = {
                            showAddMemoryDialog = false
                        },
                        onMemoryAdded = {
                            update = true // Atualiza a lista de memórias
                        }
                    )
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
fun CardMemoria(memory: Memoria, onImagePainter: (ImagePainter, Boolean) -> Unit) {
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

                Spacer(modifier = Modifier.height(16.dp))

                // Exibe a imagem da memória
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
fun AddMemoriaScreen(
    usuarioId: String,
    parceiroId: String?,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onMemoryAdded: () -> Unit // Callback para atualizar memórias
) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    var isCarregando by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagemUri = uri
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Adicionar Memória",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EntradaTexto(
                        texto = titulo,
                        onValueChange = { titulo = it },
                        label = "Título"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EntradaTexto(
                        texto = descricao,
                        onValueChange = { descricao = it },
                        label = "Descrição"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (imagemUri == null) {
                        Botao(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            texto = "Selecionar Imagem",
                            fonteTexto = 14,
                            modifier = Modifier.width(200.dp)
                        )
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

                    if (isCarregando) {
                        CircularProgressIndicator()
                    }
                }
            },
            confirmButton = {
                Botao(
                    onClick = {
                        if (titulo.isNotEmpty() && descricao.isNotEmpty() && imagemUri != null) {
                            isCarregando = true
                            enviarMidia(imagemUri!!) { downloadUrl ->
                                if (downloadUrl != "null") {
                                    val memory = Memoria(
                                        titulo,
                                        descricao,
                                        downloadUrl,
                                        usuarioId,
                                        parceiroId
                                    )
                                    adicionarMemoria(
                                        memory,
                                        usuarioId,
                                        parceiroId
                                    ) { sucesso, mensagem ->
                                        isCarregando = false
                                        if (sucesso) {
                                            Log.i("TAG", "Memória adicionada com sucesso")
                                            onDismiss()
                                            onMemoryAdded() // Chama a função para atualizar as memórias
                                        } else {
                                            Log.e("TAG", "Erro ao adicionar a memória: $mensagem")
                                        }
                                    }
                                    Log.i("TAG", "Imagem enviada com sucesso")
                                } else {
                                    Log.e("TAG", "Erro ao enviar a imagem")
                                    isCarregando = false
                                }
                            }
                        } else {
                            Log.i("TAG", "Erro ao adicionar a memória")
                        }
                    },
                    texto = "Adicionar",
                    fonteTexto = 14,
                    largura = 134,
                    enabled = !isCarregando
                )
            },
            dismissButton = {
                Botao(
                    onClick = { onDismiss() },
                    texto = "Cancelar",
                    fonteTexto = 14,
                    largura = 130
                )
            },
            containerColor = primaryContainerLight
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
