@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.blocodenotas_dataroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blocodenotas_dataroom.dataStore.StoreAnotacao
import com.example.blocodenotas_dataroom.ui.theme.BlocoDeNotasDataRoomTheme
import com.example.blocodenotas_dataroom.ui.theme.Yellow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlocoDeNotasDataRoomTheme {
                BlocoDeNotasApp()
            }
        }
    }
}

@Composable
fun BlocoDeNotasApp() {

    val context = LocalContext.current
    val storeAnotacao = StoreAnotacao(context)

    val scope = rememberCoroutineScope()
    val anotacaoSalva = storeAnotacao.getAnotacao.collectAsState("")
    var anotacao by remember {
        mutableStateOf("")
    }
    anotacao = anotacaoSalva.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bloco de notas",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        onTextLayout = {}
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Yellow)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          scope.launch {
                              storeAnotacao.salvarAnotacao(anotacao)
                              Toast.makeText(context, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show()
                          }
                },
                containerColor = Yellow,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_save_24),
                    contentDescription = "Salvar",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TextField(
                value = anotacao,
                onValueChange = { novaAnotacao -> anotacao = novaAnotacao },
                textStyle = TextStyle(fontSize = 20.sp),
                    label = { Text(text = "Insira sua anotação...", onTextLayout = {}) },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Yellow,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Yellow,
                    focusedTextColor = Color.Black
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlocoDeNotasAppPreview() {
    BlocoDeNotasDataRoomTheme {
        BlocoDeNotasApp()
    }
}