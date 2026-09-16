package io.jay.wishlistapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.jay.wishlistapp.data.Wish
import io.jay.wishlistapp.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(
    private val wishRepository: WishRepository
) : ViewModel() {

    var titleState by mutableStateOf("")
    var descriptionState by mutableStateOf("")

    fun onTitleChange(newValue: String) {
        titleState = newValue
    }

    fun onDescriptionChange(newValue: String) {
        descriptionState = newValue
    }

    fun resetInputs() {
        titleState = ""
        descriptionState = ""
    }

    fun addWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addWish(wish)
        }
    }

    val allWishes: Flow<List<Wish>> = wishRepository.getAllWishes()
//    fun getAllWishes(): Flow<List<Wish>> {
//        return wishRepository.getAllWishes()
//    }

    fun getWishById(id: Long): Flow<Wish> {
        return wishRepository.getWish(id)
    }

    fun updateWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateWish(wish)
        }
    }

    fun deleteWish(wish: Wish) {
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteWish(wish)
        }
    }
}