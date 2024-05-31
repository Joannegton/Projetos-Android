@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.calculadoraimc

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme
import com.example.calculadoraimc.ui.theme.Dark_Blue
import com.example.calculadoraimc.ui.theme.Light_Blue
import com.example.calculadoraimc.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //confg para usar 100% da tela inclusive o status bar
        setContent {
            CalculadoraIMCTheme {
                CalculadoraIMC()
            }
        }
    }
}

@Composable
fun CalculadoraIMC() {

    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imc by remember { mutableFloatStateOf(0.0f) }
    var imcStatus by remember { mutableStateOf("") }

    var imcAparecer by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Calculadora IMC", fontSize = 27.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Dark_Blue,
                    titleContentColor = White
                ),
                actions = {
                    IconButton(onClick = {
                        imc = 0.0f
                        peso = ""
                        altura = ""
                        imcStatus = ""
                        imcAparecer = false
                    }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Atualizar", tint = White, modifier = Modifier.size(40.dp))
                    }
                }
            )
        }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Calculadora IMC",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Light_Blue,
                modifier = Modifier.padding(50.dp)
            )

            OutlinedTextField(
                value = peso,
                onValueChange ={novPeso ->
                    peso = novPeso
                },
                label = { Text(text = "Peso") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Dark_Blue,
                    unfocusedBorderColor = Light_Blue,
                    cursorColor = Light_Blue,
                    focusedLabelColor = Dark_Blue,
                    unfocusedLabelColor = Light_Blue
                ),
                textStyle = TextStyle(Dark_Blue, 18.sp),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                value = altura,
                onValueChange ={novaAltura ->
                    altura = novaAltura
                },
                label = { Text(text = "Altura") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Dark_Blue,
                    unfocusedBorderColor = Light_Blue,
                    cursorColor = Light_Blue,
                    focusedLabelColor = Dark_Blue,
                    unfocusedLabelColor = Light_Blue
                ),
                textStyle = TextStyle(Dark_Blue, 18.sp),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Button(
                onClick = {
                    if (peso.isNotEmpty() && altura.isNotEmpty()){
                        imc = peso.toFloat() / (altura.toFloat() * altura.toFloat())
                        imcStatus = when {
                            imc < 18.5 -> "Peso Baixo"
                            imc < 24.9 -> "Peso Normal"
                            imc < 29.9 -> "Sobrepeso"
                            imc < 34.9 -> "Obesidade Grau I"
                            imc < 39.9 -> "Obesidade Grau II"
                            else -> "Obesidade Grau III"
                        }
                        imcAparecer = true
                    } else{
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Light_Blue,
                    contentColor = White
                )
            ) {
                Text(text = "Calcular", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.padding(10.dp))

            if (imcAparecer){
                Row {
                    Text(text = "IMC: ", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(text = imc.toString(), fontSize = 25.sp)
                }
                Text(text = imcStatus, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculadoraIMCTheme {
        CalculadoraIMC()
    }
}