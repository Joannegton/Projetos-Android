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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.compose.DiarioDeMemoriasTheme
import com.example.compose.primaryContainerLight
import com.example.diariodememorias.models.Memoria
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.viewModel.MemoriaViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryApp(viewModel: MemoriaViewModel) {
    val memories by viewModel.memories.collectAsState()
    var showAddMemoryDialog by remember { mutableStateOf(false) }

    // Atualiza as memórias ao iniciar o Composable
    LaunchedEffect(Unit) {
        viewModel.pegarMemorias()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddMemoryDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Memória")
            }
        }
    ) {
        Box {
            var showDialog by remember { mutableStateOf(false) }
            var imagePainter = rememberImagePainter("https://example.com/your-image.jpg")

            Column(Modifier.fillMaxSize()) {
                LazyColumn {
                    items(memories) { memory ->
                        CardMemoria(memory) { imgPainter, show ->
                            imagePainter = imgPainter
                            showDialog = show
                        }
                    }
                }
                if (showAddMemoryDialog) {
                    AddMemoriaScreen(
                        viewModel = viewModel,
                        onDismiss = { showAddMemoryDialog = false }
                    )
                }
            }
            VisualizadorImagemUrl(
                imagePainter = imagePainter,
                dialog = showDialog,
                onDialogDismiss = { showDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CardMemoria(memory: Memoria, onImagePainter: (ImagePainter, Boolean) -> Unit) {
    val imagePainter = rememberImagePainter(memory.imageUri)

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

            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onImagePainter(imagePainter, true) }
            )
        }
    }
}

@Composable
fun AddMemoriaScreen(
    viewModel: MemoriaViewModel,
    onDismiss: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    val isCarregando by viewModel.isCarregando.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagemUri = uri
    }

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
                    viewModel.viewModelScope.launch {
                        if (titulo.isNotEmpty() && descricao.isNotEmpty() && imagemUri != null) {
                            val usuarioId = viewModel.getUsuarioId() ?: return@launch
                            val parceiroId = viewModel.getParceiroId() ?: return@launch
                            viewModel.adicionarMemoria(
                                Memoria(
                                    title = titulo,
                                    description = descricao,
                                    imageUri = imagemUri.toString(),
                                    usuarioId = usuarioId,
                                    compartilhadoCom = parceiroId
                                )
                            )
                            viewModel.enviarMidia(imagemUri!!)
                            onDismiss()
                        } else {
                            Log.i("TAG", "Preencha todos os campos")
                        }
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
                onClick = onDismiss,
                texto = "Cancelar",
                fonteTexto = 14,
                largura = 130
            )
        },
        containerColor = primaryContainerLight
    )
}

@Composable
fun VisualizadorImagemUrl(
    imagePainter: ImagePainter,
    dialog: Boolean,
    onDialogDismiss: () -> Unit
) {
    if (dialog) {
        AlertDialog(
            onDismissRequest = onDialogDismiss,
            text = {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Botao(
                    onClick = onDialogDismiss,
                    texto = "Fechar",
                    fonteTexto = 14,
                    largura = 134
                )
            }
        )
    }
}
