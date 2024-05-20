package com.github.jesushzc.pokedex.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DialogNoInternet(
    showDialog: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Ok")
                }
            },
            title = {
                Text(
                    text = "Oops!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "No internet connection. Please check your connection and try again.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        )
    }
}