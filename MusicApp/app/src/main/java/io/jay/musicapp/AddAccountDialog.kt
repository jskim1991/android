package io.jay.musicapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddAccountDialog(open: MutableState<Boolean>) {
    if (!open.value) {
        return
    }

    AlertDialog(
        onDismissRequest = {
            open.value = false
        },

        dismissButton = {
            TextButton(onClick = {
                open.value = false
            }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                open.value = false
            }) {
                Text("Confirm")
            }
        },

        title = { Text("Add Account") },
        text = {
            Column(modifier = Modifier.wrapContentHeight().padding(top = 16.dp), verticalArrangement = Arrangement.Center) {
                TextField(
                    label = { Text("Email") },
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.padding(top = 16.dp),
                )
                TextField(
                    label = { Text("Password") },
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        },
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(8.dp),
        shape = RoundedCornerShape(5.dp),
        containerColor = Color.White,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}