package io.jay.wishlistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WishViewModel: ViewModel() {

    var titleState by mutableStateOf("")
    var descriptionState by mutableStateOf("")

    fun onTitleChange(newValue: String) {
        titleState = newValue
    }

    fun onDescriptionChange(newValue: String) {
        descriptionState = newValue
    }
}