package com.example.diariodememorias.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.DiarioDeMemoriasTheme
import com.example.compose.backgoundContainer
import com.example.compose.secondaryDark
import com.example.compose.secondaryLight
import com.example.diariodememorias.R
import com.example.diariodememorias.funcoes.entrar

@Composable
fun Login(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(backgoundContainer)
    ) {
        Text(
            text = "Bem vinda meu amor\nPara ver a surpresa que preparei,\nantes terá que descobrir a senha.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Image(
            painter = painterResource(R.drawable.eueela), contentDescription = "nós",
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .border(5.dp, secondaryLight, MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Dica: Quantos dias faz que namoramos?",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = secondaryDark,
                unfocusedContainerColor = secondaryDark,
                focusedTextColor = secondaryLight,
                unfocusedTextColor = secondaryLight,
                cursorColor = secondaryLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = secondaryLight,
                focusedLabelColor = secondaryLight,
                unfocusedLabelColor = secondaryLight,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = secondaryDark,
                unfocusedContainerColor = secondaryDark,
                focusedTextColor = secondaryLight,
                unfocusedTextColor = secondaryLight,
                cursorColor = secondaryLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = secondaryLight,
                focusedLabelColor = secondaryLight,
                unfocusedLabelColor = secondaryLight,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                entrar(email, password+"dias") { sucesso, _ ->
                    if (sucesso) {
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Não sabe, não sabeeee", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(secondaryLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text("Login", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF800E06)
@Composable
fun GreetiPreview() {
    DiarioDeMemoriasTheme {
        Login(onLoginSuccess = {})
        //AddMemoryDialog(onAddMemory = { /*TODO*/ }) { /*TODO*/ }
    }
}