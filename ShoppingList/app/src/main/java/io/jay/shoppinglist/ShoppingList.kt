package io.jay.shoppinglist

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import java.util.UUID

data class ShoppingItem(
    val id: UUID,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false,
    var address: String = ""
)

@Composable
fun ShoppingList(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navController: NavController,
    context: Context,
    address: String
) {

    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    fun startLocationUpdate() {
        locationUtils.requestLocationUpdates(onLocation = {
            viewModel.updateLocation(it)
        })
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val coarsePermission = permissions.getOrDefault(ACCESS_COARSE_LOCATION, false)
            val finePermission = permissions.getOrDefault(ACCESS_FINE_LOCATION, false)

            if (coarsePermission && finePermission) {
                // has access to location
                startLocationUpdate()
            } else {
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    ACCESS_COARSE_LOCATION
                )

                if (rationaleRequired) {
                    Toast.makeText(context, "Location permission is required", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Location permission is required. Please enable it in the settings", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    )

    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), verticalArrangement = Arrangement.Center) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp))
        {
            items(shoppingItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditComplete = { name, qty ->
                        shoppingItems = shoppingItems.map {
                            if (it.id == item.id) it.copy(name = name, quantity = qty, isEditing = false, address = address)
                            else it
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        shoppingItems = shoppingItems.map {
                            if (it.id == item.id) it.copy(isEditing = true)
                            else it
                        }
                    }, onDeleteClick = {
                        shoppingItems = shoppingItems.filter { it.id != item.id }
                    })
                }


            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        label = { Text("name") } ,
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField(
                        label = { Text("quantity") },
                        value = quantity,
                        onValueChange = { quantity = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )

                    Button(onClick = {
                        if (locationUtils.hasLocationPermission(context)) {
                            startLocationUpdate()
                            navController.navigate("locationscreen") {
                                this.launchSingleTop
                            }
                        } else {
                            requestPermissionLauncher.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
                        }
                    }) {
                        Text("address")
                    }
                }
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = {
                        itemName = ""
                        quantity = ""
                        showDialog = false
                    }) { Text("Cancel") }
                    TextButton(onClick = {
                        if (itemName.isBlank()) {
                            return@TextButton
                        }

                        val itemQuantity = quantity.toIntOrNull() ?: run {
                            return@TextButton
                        }

                        val newItem = ShoppingItem(id = UUID.randomUUID(), name = itemName, quantity = itemQuantity, address = address)
                        shoppingItems = shoppingItems + newItem

                        itemName = ""
                        quantity = ""
                        showDialog = false
                    }) { Text("Add") }
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth()
            .border(border = BorderStroke(2.dp, Color(0xFF013787)), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Row {
                Text(item.name, modifier = Modifier.padding(8.dp))
                Text("qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(imageVector = Icons.Default.LocationOn, "Location")
                Text(item.address)
            }
        }


        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItem,
    onEditComplete: (String, Int) -> Unit
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(value = editedName, onValueChange = { editedName = it }, singleLine = true, modifier = Modifier.wrapContentSize().padding(8.dp))
            BasicTextField(value = editedQuantity, onValueChange = { editedQuantity = it }, singleLine = true, modifier = Modifier.wrapContentSize().padding(8.dp))
        }

        Button(onClick = {
            if (editedName.isBlank()) {
                return@Button
            }
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            isEditing = false
        }) {
            Text("Update")
        }
    }
}