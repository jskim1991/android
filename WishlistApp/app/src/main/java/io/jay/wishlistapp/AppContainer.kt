package io.jay.wishlistapp

import android.content.Context
import androidx.room.Room
import io.jay.wishlistapp.data.WishDatabase
import io.jay.wishlistapp.data.WishRepository

object AppContainer {
    lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
    }
}