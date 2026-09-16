package io.jay.wishlistapp

import android.app.Application

class WishlistApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.provide(this)
    }
}