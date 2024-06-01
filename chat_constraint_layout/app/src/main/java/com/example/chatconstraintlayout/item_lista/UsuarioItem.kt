package com.example.chatconstraintlayout.item_lista

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.chatconstraintlayout.R
import com.example.chatconstraintlayout.model.Usuario
import com.example.chatconstraintlayout.ui.theme.White

@Composable
fun UsuarioItem(
    listaUsuarios: MutableList<Usuario>,
    position: Int,
    context: Context
) {

    val usuario = listaUsuarios[position].nome
    val fotoUsuario = listaUsuarios[position].foto

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        val (txtNome, foto, mensagem, linha) = createRefs()

        Image(
            painter = painterResource(id = fotoUsuario),
            contentDescription = "foto",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .constrainAs(foto) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start, margin = 20.dp)
                },
            contentScale = ContentScale.Crop
        )

        TextButton(
            onClick = {
                      Toast.makeText(context, "Nome $usuario", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = Color.Black
            ),
            modifier = Modifier.constrainAs(txtNome) {
               start.linkTo(foto.end, margin = 20.dp)
                top.linkTo(parent.top, margin = 20.dp)
                end.linkTo(parent.end, margin = 20.dp)
            }
        ) {
            Text(text = usuario ?: "",
                fontSize = 18.sp
            )
        }

        Text(
            text = "Msg padrão",
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(mensagem) {
                top.linkTo(txtNome.bottom)
                start.linkTo(foto.end, margin = 20.dp)
                end.linkTo(parent.end, margin = 20.dp)
            }
        )

        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .width(250.dp)
                .height(1.dp)
                .constrainAs(linha) {
                    top.linkTo(mensagem.bottom, margin = 10.dp)
                    start.linkTo(foto.end, margin = 30.dp)
                    end.linkTo(parent.end, margin = 20.dp)
                })
    }
}

