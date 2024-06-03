package com.example.contatosroomdatabaseenavegao.views

import EntradaTexto
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contatosroomdatabaseenavegao.AppDatabase
import com.example.contatosroomdatabaseenavegao.dao.ContatoDao
import com.example.contatosroomdatabaseenavegao.model.Contato
import com.example.contatosroomdatabaseenavegao.ui.theme.Purple80
import com.example.contatosroomdatabaseenavegao.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var contatoDao: ContatoDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalvarContato(navController: NavController) {

    var nome by remember { mutableStateOf("") }
    var sobreNome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    val context = LocalContext.current
    val listaContatos: MutableList<Contato> = mutableListOf()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Salvar Contato", fontSize = 18.sp, color = White)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80)
            )
        }
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            EntradaTexto(
                valor = nome,
                label = "Nome",
                onValuaChange = { novoNome ->
                    nome = novoNome
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))

            EntradaTexto(
                valor = sobreNome,
                label = "Sobrenome",
                onValuaChange = { novoSobrenome ->
                    sobreNome = novoSobrenome
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))

            EntradaTexto(
                valor = idade,
                label = "Idade",
                onValuaChange = { novaIdade ->
                    idade = novaIdade
                },
                tipoTeclado = KeyboardType.Number
            )
            Spacer(modifier = Modifier.padding(10.dp))

            EntradaTexto(
                valor = telefone,
                label = "Telefone",
                onValuaChange = { novoTelefone ->
                    telefone = novoTelefone
                },

                acaoTeclado = ImeAction.Done
            )

            Button(
                onClick = {
                    var msg = false
                    scope.launch(Dispatchers.IO) {
                        if (nome.isEmpty() || sobreNome.isEmpty() || idade.isEmpty() || telefone.isEmpty()){
                            msg = false
                        } else {
                            msg = true
                            val contato = Contato(
                                nome = nome,
                                sobrenome = sobreNome,
                                idade = idade,
                                celular = telefone)
                            listaContatos.add(contato)
                            contatoDao = AppDatabase.getInstance(context).contatoDao()
                            contatoDao.gravar(listaContatos)
                        }

                        scope.launch(Dispatchers.Main) {
                            if(msg){
                                Toast.makeText(context, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else{
                                Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Purple80),
            ) {
                Text(text = "Salvar", fontSize = 18.sp, color = White)
            }
        }
    }
}

