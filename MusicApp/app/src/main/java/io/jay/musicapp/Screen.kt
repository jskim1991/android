package io.jay.musicapp

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class DrawerScreen(val drawerTitle: String, val drawerRoute: String, @DrawableRes val icon: Int): Screen(drawerTitle, drawerRoute) {
        object Account: DrawerScreen("Account", "account", R.drawable.ic_account)
        object Subscription: DrawerScreen("Subscription", "subscribe", R.drawable.ic_subscribe)
        object AddAccount: DrawerScreen("Add Account", "add_account", R.drawable.outline_person_add_24)
    }
}

