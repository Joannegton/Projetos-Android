package com.example.contatosroomdatabaseenavegao.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contatosroomdatabaseenavegao.AppDatabase
import com.example.contatosroomdatabaseenavegao.dao.ContatoDao
import com.example.contatosroomdatabaseenavegao.itemLista.ContatoItem
import com.example.contatosroomdatabaseenavegao.model.Contato
import com.example.contatosroomdatabaseenavegao.ui.theme.Purple80
import com.example.contatosroomdatabaseenavegao.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var contatoDao: ContatoDao
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContatos(navController: NavController) {
    val context = LocalContext.current
    val listaContatos: MutableList<Contato> = mutableListOf()

    val scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        contatoDao = AppDatabase.getInstance(context).contatoDao()
        val contatos = contatoDao.getContatos()

        for (contato in contatos) {
            listaContatos.add(contato)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Agenda de Contatos", fontSize = 18.sp, color = White)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple80)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("salvarContato")},
                containerColor = Purple80,
                contentColor = White
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Adicionar Contato")
            }
        }
    ) {
        LazyColumn(Modifier.padding(it)) {
            itemsIndexed(listaContatos){position, item ->
                ContatoItem(navController, position, listaContatos, context )

            }
        }
    }
    
}