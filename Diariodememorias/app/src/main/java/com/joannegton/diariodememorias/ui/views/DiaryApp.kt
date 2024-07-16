package com.joannegton.diariodememorias.ui.views

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.joannegton.diariodememorias.MainActivity
import com.joannegton.diariodememorias.data.models.Memoria
import com.joannegton.diariodememorias.ui.componentes.Botao
import com.joannegton.diariodememorias.ui.componentes.EntradaTexto
import com.joannegton.diariodememorias.ui.componentes.VisualizadorImagem
import com.joannegton.diariodememorias.ui.componentes.VisualizadorImagemUrl
import com.joannegton.diariodememorias.util.Resultado
import com.joannegton.diariodememorias.viewModel.GerenciamentoSessaoViewModel
import com.joannegton.diariodememorias.viewModel.MemoriaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryApp(
    viewModel: MemoriaViewModel,
    viewModel2: GerenciamentoSessaoViewModel,
    showDialog: Boolean, // Recebe o estado
    onOpenViewer: () -> Unit, // Função para abrir o visualizador
    onCloseViewer: () -> Unit // Função para fechar o visualizador
) {
    val memories by viewModel.memories.collectAsState()
    var showAddMemoryDialog by remember { mutableStateOf(false) }

    val initialImagePainter =
        rememberImagePainter("")
    var imagePaint by remember { mutableStateOf(initialImagePainter) }


    Scaffold(
        floatingActionButton = {
            if (!showDialog) {
                FloatingActionButton(
                    onClick = { showAddMemoryDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.Add,
                        "Adicionar Memória"
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box {
            Column(Modifier.fillMaxSize()) {
                LazyColumn {
                    items(memories) { memory ->
                        CardMemoria(memory, viewModel) {
                            imagePaint = it
                            onOpenViewer()
                        }
                    }
                }
                if (showAddMemoryDialog) {
                    AddMemoriaScreen(
                        viewModel = viewModel,
                        onDismiss = { showAddMemoryDialog = false },
                        viewModel2 = viewModel2
                    )
                }
            }

            // Exibe o VisualizadorImagemUrl se houver uma imagem selecionada
            VisualizadorImagemUrl(
                imagePainter = imagePaint,
                dialogVisivel = showDialog,
                onDialogDismiss = {
                    onCloseViewer()
                }
            )

        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CardMemoria(
    memory: Memoria,
    viewModel: MemoriaViewModel,
    onOpenViewer: (ImagePainter) -> Unit
) {
    val imagePainter = rememberImagePainter(memory.imageUri)
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .zIndex(0f)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 25.sp
                )
                IconButton(onClick = { mostrarDialogo = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Apagar memoria",
                        tint = Color.Red,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { /*TODO*/ },
                    confirmButton = {
                        Botao(
                            onClick = {
                                viewModel.apagarMemoria(memory.id)
                                mostrarDialogo = false
                            },
                            texto = "Apagar",
                            largura = 124,
                            fonteTexto = 16
                        )
                    },
                    dismissButton = { // Botão de fechar
                        Botao(
                            onClick = { mostrarDialogo = false },
                            texto = "Cancelar",
                            largura = 140,
                            fonteTexto = 16
                        )
                    },
                    title = { Text(text = "Apagar Memória") },
                    text = {
                        Text(text = "Tem certeza que deseja apagar esta memória?")
                    }
                )

            }

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
                    .clickable { onOpenViewer(imagePainter) }
            )
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun AddMemoriaScreen(
    viewModel: MemoriaViewModel,
    viewModel2: GerenciamentoSessaoViewModel,
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

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    val usuarioId = viewModel2.usuarioId()
    val parceiroId = viewModel2.parceiroId()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Adicionar Memória",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
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

                Spacer(modifier = Modifier.height(14.dp))

                if (imagemUri == null) {
                    Botao(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        texto = "Selecionar Imagem",
                        fonteTexto = 14,
                        cor = Color.LightGray
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
                            .clickable {
                                showDialog = true
                            }
                    )
                }
                VisualizadorImagem(
                    imagem = imagemUri,
                    dialog = showDialog,
                    onDialogDismiss = { showDialog = false })

                if (isCarregando) {
                    CircularProgressIndicator()
                }
            }
        },
        confirmButton = {
            Row {
                Botao(
                    onClick = onDismiss,
                    texto = "Voltar",
                    fonteTexto = 16,
                    largura = 125,
                    cor = MaterialTheme.colorScheme.tertiaryContainer
                )
                Botao(
                    onClick = {
                        // Iniciamos uma nova coroutine no escopo do ViewModel
                        viewModel.viewModelScope.launch {
                            // Verificamos se todos os campos foram preenchidos
                            if (titulo.isNotEmpty() && descricao.isNotEmpty() && imagemUri != null) {
                                // upload da imagem e obtemos a URL de download
                                when (val result = viewModel.enviarMidia(imagemUri!!)) {
                                    // Se o upload for bem-sucedido, criamos e adicionamos a memória
                                    is Resultado.Sucesso -> {
                                        val downloadUrl = result.data
                                        viewModel.adicionarMemoria(
                                            Memoria(
                                                title = titulo,
                                                description = descricao,
                                                imageUri = downloadUrl,
                                                usuarioId = usuarioId,
                                                compartilhadoCom = parceiroId
                                            )
                                        )
                                        (context as MainActivity).mostrarInterstitialAnuncio()
                                        onDismiss()
                                    }
                                    // Se o upload falhar, tratamos a exceção
                                    is Resultado.Falha -> {
                                        val exception = result.data
                                        Log.i("TAG", "Erro ao fazer upload da imagem $exception")
                                    }

                                }
                            }
                        }
                    },
                    texto = "Adicionar",
                    fonteTexto = 16,
                    largura = 144,
                    enabled = !isCarregando
                )

            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    )
}

