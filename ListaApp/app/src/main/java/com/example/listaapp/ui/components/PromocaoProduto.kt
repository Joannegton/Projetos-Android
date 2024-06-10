import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PromocaoProduto(quantidade: String, valor: String) {
    var quantidadeText by remember { mutableStateOf(quantidade) }
    var valorText by remember { mutableStateOf(valor) }

    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Quanto por item:")
            EntradaTexto(
                value = quantidadeText,
                onValueChange = { quantidadeText = it },
                label = "Quantidade",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Valor por Item:")
            EntradaTexto(
                value = valorText,
                onValueChange = { valorText = it },
                label = "Valor",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun PromocaoProdutoPreview() {
    var quantidade by remember { mutableStateOf("") }
    var valorProduto by remember { mutableStateOf("") }
    PromocaoProduto(quantidade, valorProduto)
}
