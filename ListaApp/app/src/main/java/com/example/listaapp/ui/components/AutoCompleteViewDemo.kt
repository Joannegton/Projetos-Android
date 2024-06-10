/*
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun AutoCompleteTextViewDemo() {
    val viewModel: AutoCompleteViewModel = viewModel()
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Digite o nome do produto:")
        AutoCompleteTextView(
            suggestions = viewModel.suggestions,
            onSuggestionSelected = { suggestion ->
                viewModel.selectedSuggestion = suggestion
            }
        )
    }
}

@Composable
fun AutoCompleteTextView(
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit
) {
    val adapter = remember { ArrayAdapter<String>(AmbientContext.current, android.R.layout.simple_dropdown_item_1line, suggestions) }
    androidx.appcompat.widget.AutoCompleteTextView(AmbientContext.current).apply {
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            val suggestion = adapter.getItem(position)
            if (suggestion != null) {
                setText(suggestion)
                onSuggestionSelected(suggestion)
            }
        }
    }
}

class AutoCompleteViewModel : ViewModel() {
    val suggestions = listOf("Arroz", "Feijão", "Macarrão", "Óleo", "Sal")
    var selectedSuggestion: String? by mutableStateOf(null)
}

@Preview(showBackground = true)
@Composable
fun PreviewAutoCompleteTextViewDemo() {
    AutoCompleteTextViewDemo()
}
*/