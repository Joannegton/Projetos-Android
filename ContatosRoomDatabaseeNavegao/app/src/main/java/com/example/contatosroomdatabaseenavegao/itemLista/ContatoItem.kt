package com.example.contatosroomdatabaseenavegao.itemLista

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.contatosroomdatabaseenavegao.AppDatabase
import com.example.contatosroomdatabaseenavegao.dao.ContatoDao
import com.example.contatosroomdatabaseenavegao.model.Contato
import com.example.contatosroomdatabaseenavegao.ui.theme.Red
import com.example.contatosroomdatabaseenavegao.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var contatoDao: ContatoDao
@Composable
fun ContatoItem(
    navController: NavController,
    position: Int,
    lista: MutableList<Contato>,
    context: Context
) {
    val nome = lista[position].nome
    val sobrenome = lista[position].sobrenome
    val idade = lista[position].idade
    val celular = lista[position].celular
    val id = lista[position].uid
    val contato = lista[position]

    val scope = rememberCoroutineScope()

    fun alertDialodDeletarContato(){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Desaja Excluir?").setMessage("Certeza?")
        alertDialog.setPositiveButton("Ok"){_,_ ->
            scope.launch(Dispatchers.IO) {
                contatoDao = AppDatabase.getInstance(context).contatoDao()
                contatoDao.deletar(id)
                lista.remove(contato)
            }

            scope.launch(Dispatchers.Main) {
                navController.navigate("listaContatos")
                Toast.makeText(context, "Contato Excluido", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.setNegativeButton("Cancelar"){_,_ ->}
        alertDialog.show()
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)

    ) {
        ConstraintLayout(modifier = Modifier.padding(20.dp)) {
            val (txtNome, txtSobrenome, txtIdade, txtCelular, btnAtualizar, btnDeletar) = createRefs()

            Text(
                text = "Contato: $nome $sobrenome",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(txtNome){
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                }
            )

            Text(
                text = "Idade: $idade anos",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(txtIdade){
                    top.linkTo(txtNome.bottom, margin = 3.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                }
            )

            Text(
                text = "Telefone: $celular",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(txtCelular){
                    top.linkTo(txtIdade.bottom, margin = 3.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                }
            )

            IconButton(
                onClick = {
                    navController.navigate("atualizaContato/{$id}")

                },
                modifier = Modifier.constrainAs(btnAtualizar){
                    start.linkTo(txtCelular.end, margin = 30.dp)
                    top.linkTo(parent.top, margin = 52.dp)
                }
            ) {
                Icon(imageVector = Icons.Filled.Create, contentDescription = "Editar", tint = Color.Black)
            }
            IconButton(
                onClick = {
                    alertDialodDeletarContato()
                },
                modifier = Modifier.constrainAs(btnDeletar){
                    start.linkTo(btnAtualizar.end, margin = 10.dp)
                    top.linkTo(parent.top, margin = 52.dp)
                }
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Editar", tint = Red)
            }
        }
    }
}

