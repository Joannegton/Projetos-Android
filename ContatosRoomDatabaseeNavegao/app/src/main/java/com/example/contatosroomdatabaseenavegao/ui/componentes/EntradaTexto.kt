import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.contatosroomdatabaseenavegao.ui.theme.Purple80

@Composable
fun EntradaTexto(
    valor: String,
    onValuaChange: (String) -> Unit,
    label: String,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    acaoTeclado: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { onValuaChange(it) },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = acaoTeclado
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Purple80,
            focusedBorderColor = Purple80
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding( horizontal = 20.dp),
        maxLines = 1
    )
}
