package com.example.diariodememorias.ui.views

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.diariodememorias.data.models.Memoria
import com.example.diariodememorias.ui.componentes.Botao
import com.example.diariodememorias.ui.componentes.EntradaTexto
import com.example.diariodememorias.ui.componentes.VisualizadorImagemUrl
import com.example.diariodememorias.util.Resultado
import com.example.diariodememorias.viewModel.GerenciamentoSessaoViewModel
import com.example.diariodememorias.viewModel.MemoriaViewModel
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
        rememberImagePainter("https://s2-techtudo.glbimg.com/SSAPhiaAy_zLTOu3Tr3ZKu2H5vg=/0x0:1024x609/888x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2022/c/u/15eppqSmeTdHkoAKM0Uw/dall-e-2.jpg")
    var imagePaint by remember { mutableStateOf(initialImagePainter) }

    // Atualiza as memórias ao iniciar o Composable
    LaunchedEffect(Unit) {
        viewModel.pegarMemorias()
    }

    Scaffold(
        floatingActionButton = {
            if (!showDialog) {
                FloatingActionButton(
                    onClick = { showAddMemoryDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
                        CardMemoria(memory) {
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
fun CardMemoria(memory: Memoria, onOpenViewer: (ImagePainter) -> Unit) {
    val imagePainter = rememberImagePainter(memory.imageUri)

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
                                    onDismiss()
                                }
                                // Se o upload falhar, tratamos a exceção
                                is Resultado.Falha -> {
                                    val exception = result.data
                                    Log.i("TAG", "Erro ao fazer upload da imagem $exception")
                                }

                            }
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
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    )
}

