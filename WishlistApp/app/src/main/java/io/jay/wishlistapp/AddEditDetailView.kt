package io.jay.wishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.jay.wishlistapp.data.Wish

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController,
    onShowSnackbar: (String) -> Unit
) {

    val snackMessage = remember { mutableStateOf("") }

    if (id != 0L) {
        val wish = viewModel.getWishById(id).collectAsState(initial = Wish(id, "", ""))
        viewModel.titleState = wish.value.title
        viewModel.descriptionState = wish.value.description
    } else {
        viewModel.resetInputs()
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = if (id != 0L) stringResource(R.string.updated_wish) else stringResource(R.string.add_wish),
                onBackNavClicked = { navController.navigateUp() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            WishTextField(label = "Title", value = viewModel.titleState, onChange = { viewModel.onTitleChange(it) })

            Spacer(modifier = Modifier.height(10.dp))
            WishTextField(label = "Description", value = viewModel.descriptionState, onChange = { viewModel.onDescriptionChange(it) })

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if (viewModel.titleState.isNotEmpty() && viewModel.descriptionState.isNotEmpty()) {
                    if (id != 0L) {
                        // update wish
                        viewModel.updateWish(Wish(id = id, title = viewModel.titleState.trim(), description = viewModel.descriptionState.trim()))
                        snackMessage.value = "Wish has been updated"
                    } else {
                        // add wish
                        viewModel.addWish(Wish(title = viewModel.titleState.trim(), description = viewModel.descriptionState.trim()))
                        snackMessage.value = "Wish has been created"
                    }

                    navController.navigateUp()
                    viewModel.resetInputs()
                    onShowSnackbar(snackMessage.value)

                } else {
                    snackMessage.value = "Enter fields to create a wish"
                    onShowSnackbar(snackMessage.value)
                }


            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun WishTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(
                label,
                color = Color.Black
            )
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = colorResource(R.color.purple_500),
            unfocusedIndicatorColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black
        )
    )
}

@Preview
@Composable
fun WishTextFieldPreview() {
    WishTextField(label = "label", value = "value", onChange = {})
}