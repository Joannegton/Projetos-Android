package com.marcos.appdecafeteria.itemlista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marcos.appdecafeteria.datasource.MercadoPagoApi
import com.marcos.appdecafeteria.model.Produto
import com.marcos.appdecafeteria.ui.theme.BLUE
import com.marcos.appdecafeteria.ui.theme.GREEN
import com.marcos.appdecafeteria.ui.theme.WHITE
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun ProdutoItem(
    listaProdutos: MutableList<Produto>,
    position: Int,
    navController: NavController
){

    val nome = listaProdutos[position].nome.toString()
    val imagem = listaProdutos[position].imagem
    val iconColor = listaProdutos[position].iconColor
    val preco = listaProdutos[position].preco.toString()

    var backgroundItem by remember {mutableStateOf(WHITE)}
    val mercadoPagoApi = MercadoPagoApi()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(10.dp)
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .background(backgroundItem)
               .clickable {
                   backgroundItem = BLUE
                   scope.launch {
                       mercadoPagoApi.pagamentoMercadoPago(
                           title = nome,
                           quantity = "1",
                           unitPrice = preco,
                           respostaServidor = {initPoint ->
                               navController.navigate("Pagamento/${URLEncoder.encode(initPoint,"UTF-8")}/$nome/$preco")
                           }
                       )
                   }
               },
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.Start
       ) {

          Icon(
              imageVector = imagem!!,
              contentDescription = null,
              modifier = Modifier
                  .size(50.dp)
                  .padding(10.dp),
              tint = iconColor
          )

          Text(
              text = nome,
              fontSize = 14.sp
          ) 
           
          Spacer(modifier = Modifier.padding(5.dp,0.dp))

           Text(
               text = "Preço: $preco",
               fontSize = 14.sp,
               color = GREEN
           )

       }
    }
}