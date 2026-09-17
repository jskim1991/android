package io.jay.musicapp

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class DrawerScreen(val drawerTitle: String, val drawerRoute: String, @DrawableRes val icon: Int): Screen(drawerTitle, drawerRoute) {
        object Account: DrawerScreen("Account", "account", R.drawable.ic_account)
        object Subscription: DrawerScreen("Subscription", "subscribe", R.drawable.ic_subscribe)
        object AddAccount: DrawerScreen("Add Account", "add_account", R.drawable.outline_person_add_24)
    }

    sealed class BottomScreen(val bottomTitle: String, val bottomRoute: String, @DrawableRes val icon: Int): Screen(bottomTitle, bottomRoute) {
        object Home: BottomScreen("Home", "home", R.drawable.outline_home_24)
        object Library: BottomScreen("Library", "library", R.drawable.outline_video_library_24)
        object Browse: BottomScreen("Browse", "browse", R.drawable.outline_browse_24)
    }
}

