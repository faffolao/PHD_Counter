package it.faffolao.phdcounterapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AboutUsDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Info, 
                contentDescription = "Information icon"
            )
        },
        title = {
            Text(text = "About this app")
        },
        text = {
            Text(
                text = "PHD Counter, version 1.1 \n" +
                        "Written by Federico Arduini (faffolao) on March 2026."
            )
        }
    )
}
