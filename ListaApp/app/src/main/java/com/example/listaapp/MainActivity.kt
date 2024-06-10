package com.example.listaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.ListaAppTheme
import com.example.listadecompras.componets.ListaComprasAppBar
import com.example.listadecompras.view.ListadeComprasApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaAppTheme {
                Scaffold(
                    topBar = { ListaComprasAppBar()},
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Column(Modifier.fillMaxSize().padding(innerPadding)) {
                        ListadeComprasApp()
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun GreetingPreview() {
    ListaAppTheme {
        ListadeComprasApp()
    }
}