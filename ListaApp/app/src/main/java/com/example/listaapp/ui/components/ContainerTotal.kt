package com.example.listadecompras.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContainerTotal(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Total R$${""}",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = "Total R$${""}",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center


        )
        Text(
            text = "Total R$${""}",
            modifier = Modifier
                .weight(1f)
                .padding(start = 40.dp),
            style = MaterialTheme.typography.labelSmall,


        )
    }

}

@Preview
@Composable
fun Teste(){
    ContainerTotal()
}