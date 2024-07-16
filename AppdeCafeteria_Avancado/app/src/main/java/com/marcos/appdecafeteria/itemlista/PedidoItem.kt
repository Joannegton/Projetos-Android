package com.marcos.appdecafeteria.itemlista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcos.appdecafeteria.model.Pedido
import com.marcos.appdecafeteria.ui.theme.BLACK
import com.marcos.appdecafeteria.ui.theme.GREEN
import com.marcos.appdecafeteria.ui.theme.WHITE

@Composable
fun PedidoItem(
    listaPedidos: MutableList<Pedido>,
    position: Int
){
    val numero = listaPedidos[position].numero
    val produto = listaPedidos[position].produto
    val preco = listaPedidos[position].preco
    val statusPagamento = listaPedidos[position].statusPagamento

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(0.dp, 10.dp)
    ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .background(WHITE)
       ) {

           Text(
               text = buildAnnotatedString {
                   withStyle(
                       style = SpanStyle(
                           fontWeight = FontWeight.Bold
                       )
                   ){
                       append("Número do Pedido: ")
                   }
                   append(numero)
               },
               fontSize = 14.sp,
               modifier = Modifier.padding(10.dp)
           )

           Text(
               text = buildAnnotatedString {
                   withStyle(
                       style = SpanStyle(
                           fontWeight = FontWeight.Bold
                       )
                   ){
                       append("Produto: ")
                   }
                   append(produto)
               },
               fontSize = 14.sp,
               modifier = Modifier.padding(10.dp)
           )

           Text(
               text = buildAnnotatedString {
                  withStyle(
                       style = SpanStyle(
                           fontWeight = FontWeight.Bold,
                           color = BLACK
                       )
                   ){
                       append("Preço: ")
                   }
                   append(preco)
               },
               fontSize = 14.sp,
               color = GREEN,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.padding(10.dp)
           )

           Text(
               text = "Status de Pagamento: $statusPagamento",
               fontSize = 14.sp,
               modifier = Modifier.padding(10.dp)
           )

       }
    }
}