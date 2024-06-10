package com.example.listadecompras.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ListaAppTheme
import com.example.listadecompras.model.ProdutoKg
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProdutosDaLista(listaCompras: MutableList<ProdutoKg>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp, 7.dp, 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Produto",
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(text = "Quantidade ${""}",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )

                    val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                    val valorFormatado = formatador.format(somarValores(listaCompras))

                    Text(
                        text = valorFormatado,
                        modifier = modifier
                            .padding(start = 0.dp, end = 7.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .wrapContentWidth()
                            .padding(5.dp, 5.dp, 0.dp, 5.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                }
            }
        }

        items(listaCompras) { produto ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                ProdutoItem(produto, modifier = Modifier)
            }
        }
    }
}

@Composable
fun ProdutoItem(produto: ProdutoKg, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = produto.nome,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
            )
        Text(
            text = produto.quantidade.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        val formatoDecimal = DecimalFormat("#.##")
        val total = produto.preco * produto.quantidade
        Text(
            text = "R$ ${formatoDecimal.format(total)}" ,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(1f),
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}

/**
 * Função somar a lista de valores.
 */
private fun somarValores(lista:List<ProdutoKg>): Double{
    var soma = 0.00
    for (item in lista)
        soma += item.preco
    return soma
}
@Preview(showBackground = true)
@Composable
fun ProdutosDaListaPreview() {
    ListaAppTheme {
        val listaCompras: MutableList<ProdutoKg> = mutableListOf(
            ProdutoKg("Arroz", 2, 2.35),
            ProdutoKg("Feijão", 3, 7.35),
        )
        ProdutosDaLista(listaCompras)
    }
}