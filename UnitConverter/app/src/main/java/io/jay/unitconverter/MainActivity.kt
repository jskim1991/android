package io.jay.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.jay.unitconverter.ui.theme.UnitConverterTheme
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                UnitConverter()
            }
        }
    }
}

@Composable
fun UnitConverter() {

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("") }
    var outputUnit by remember { mutableStateOf("") }
    var inputExpanded by remember { mutableStateOf(false) }
    var outputExpanded by remember { mutableStateOf(false) }
    val inputConversionFactor = remember { mutableDoubleStateOf(1.0) }
    val outputConversionFactor = remember { mutableDoubleStateOf(1.0) }


    fun convertUnits() {
        val input = inputValue.toDoubleOrNull() ?: run {
            outputValue = ""
            return
        }

        if (inputUnit.isEmpty() || outputUnit.isEmpty()) {
            outputValue = ""
            return
        }

        val result = input * inputConversionFactor.value / outputConversionFactor.value

        val formatter = DecimalFormat("0.####", DecimalFormatSymbols(Locale.US))
        outputValue = formatter.format(result)
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // vertical
        Text("Unit Converter", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            label = { Text("Enter value") },
            value = inputValue,
            onValueChange = {
                inputValue = it
                convertUnits()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            // horizontal
            Box {
                Button(onClick = {
                    inputExpanded = true
                }) {
                    Text(text = inputUnit.ifEmpty { "From" })
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Down"
                    )
                }
                DropdownMenu(expanded = inputExpanded, onDismissRequest = {
                    inputExpanded = false
                }) {
                    DropdownMenuItem(text = { Text("mm") }, onClick = {
                        inputUnit = "mm"
                        inputExpanded = false
                        inputConversionFactor.value = 0.001
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("cm") }, onClick = {
                        inputUnit = "cm"
                        inputExpanded = false
                        inputConversionFactor.value = 0.01
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("m") }, onClick = {
                        inputUnit = "m"
                        inputExpanded = false
                        inputConversionFactor.value = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("ft") }, onClick = {
                        inputUnit = "ft"
                        inputExpanded = false
                        inputConversionFactor.value = 0.3048
                        convertUnits()
                    })
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box {
                Button(onClick = {
                    outputExpanded = true
                }) {
                    Text(text = outputUnit.ifEmpty { "To" })
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Down"
                    )
                }
                DropdownMenu(expanded = outputExpanded, onDismissRequest = {
                    outputExpanded = false
                }) {
                    DropdownMenuItem(text = { Text("mm") }, onClick = {
                        outputUnit = "mm"
                        outputExpanded = false
                        outputConversionFactor.value = 0.001
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("cm") }, onClick = {
                        outputUnit = "cm"
                        outputExpanded = false
                        outputConversionFactor.value = 0.01
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("m") }, onClick = {
                        outputUnit = "m"
                        outputExpanded = false
                        outputConversionFactor.value = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("ft") }, onClick = {
                        outputUnit = "ft"
                        outputExpanded = false
                        outputConversionFactor.value = 0.3048
                        convertUnits()
                    })
                }
            }


        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Result: $outputValue $outputUnit", style = MaterialTheme.typography.headlineSmall)
    }
}


@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverterTheme {
        UnitConverter()
    }
}