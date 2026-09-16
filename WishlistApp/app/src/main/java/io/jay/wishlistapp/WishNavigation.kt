package io.jay.wishlistapp

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@Composable
fun WishNavigation(viewModel: WishViewModel = viewModel(factory = viewModelFactory { initializer { WishViewModel(AppContainer.wishRepository) } }), navController: NavHostController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showSnackbar: (String) -> Unit = { message ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    Box {
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                HomeView(navController = navController, viewModel = viewModel)
            }

            composable(Screen.AddScreen.route + "?id={id}", arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )) { entry ->
                val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
                AddEditDetailView(
                    id = id,
                    viewModel = viewModel,
                    navController = navController,
                    onShowSnackbar = showSnackbar
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}