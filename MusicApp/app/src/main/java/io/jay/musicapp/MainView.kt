package io.jay.musicapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    val viewModel: MainViewModel = viewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()

    val screensInDrawer = listOf(
        Screen.DrawerScreen.Account,
        Screen.DrawerScreen.Subscription,
        Screen.DrawerScreen.AddAccount
    )
    val screensInBottom =
        listOf(Screen.BottomScreen.Home, Screen.BottomScreen.Library, Screen.BottomScreen.Browse)

    val navController: NavController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = remember { viewModel.currentScreen.value }

    val title = remember { mutableStateOf(currentScreen.title) }
    val dialogOpen = remember { mutableStateOf(false) }


    val isSheetFullScreen by remember { mutableStateOf(false) }
    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(
                modifier = Modifier.navigationBarsPadding(),
                backgroundColor = Color.Gray
            ) {
                screensInBottom.forEach { item ->
                    val selected = currentRoute == item.bottomRoute
                    BottomNavigationItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.bottomRoute)
                            title.value = item.bottomTitle
                        },
                        icon = {
                            val tint = if (selected) Color.White else Color.Black
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.bottomTitle,
                                tint = tint
                            )
                        },
                        label = {
                            Text(
                                item.bottomTitle,
                                color = if (selected) Color.White else Color.Black
                            )
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }



    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = roundedCornerRadius,
            topEnd = roundedCornerRadius
        ),
        sheetContent = {
            MoreBottomSheet(modifier = modifier)
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (modalSheetState.isVisible) {
                                    modalSheetState.hide()
                                } else {
                                    modalSheetState.show()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    },
                    title = { Text(title.value) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            bottomBar = bottomBar,
            scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    items(screensInDrawer) { item ->
                        DrawerItem(selected = currentRoute == item.drawerRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if (item.drawerRoute == "add_account") {
                                dialogOpen.value = true
                            } else {
                                navController.navigate(item.drawerRoute)
                                title.value = item.drawerTitle
                            }
                        }
                    }
                }
            }
        ) {
            MusicNavigation(navController = navController, viewModel = viewModel, pd = it)
            AddAccountDialog(open = dialogOpen)
        }
    }
}

@Composable
fun DrawerItem(selected: Boolean, item: Screen.DrawerScreen, onDrawerItemClicked: () -> Unit) {
    val backgroundColor = if (selected) Color.DarkGray else Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(color = backgroundColor)
            .clickable {
                onDrawerItemClicked()
            }
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.drawerTitle,
            modifier = Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(item.drawerTitle, style = MaterialTheme.typography.h5)
    }

}

@Composable
fun MusicNavigation(
    navController: NavController,
    viewModel: MainViewModel,
    pd: PaddingValues
) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(Screen.DrawerScreen.Account.route) {
            AccountView()
        }

        composable(Screen.DrawerScreen.Subscription.route) {
            SubscriptionView()
        }

        composable(Screen.BottomScreen.Home.route) {
            HomeView()
        }

        composable(Screen.BottomScreen.Library.route) {
            LibraryView()
        }

        composable(Screen.BottomScreen.Browse.route) {
            BrowseView()
        }
    }
}

@Composable
fun MoreBottomSheet(modifier: Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(Color.White)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.outline_settings_24),
                    contentDescription = "settings",
                    tint = Color.Black
                )
                Text("Settings", fontSize = 20.sp, color = Color.Black)
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.outline_share_24),
                    contentDescription = "share",
                    tint = Color.Black
                )
                Text("Share", fontSize = 20.sp, color = Color.Black)
            }

            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.outline_help_24),
                    contentDescription = "help",
                    tint = Color.Black
                )
                Text("Help", fontSize = 20.sp, color = Color.Black)
            }
        }
    }
}