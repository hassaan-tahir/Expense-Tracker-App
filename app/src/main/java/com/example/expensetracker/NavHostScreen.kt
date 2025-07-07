package com.example.expensetracker

import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.feature.addexpense.AddExpense
import com.example.expensetracker.feature.home.HomeScreen
import com.example.expensetracker.feature.stats.StatsScreen
import com.example.expensetracker.feature.chat.ChatScreen
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.ChatViewModel


@RequiresApi(35)
@Composable
@Preview(showBackground = true)
fun NavHostScreen() {

    val navController = rememberNavController()
    val chatViewModel: ChatViewModel = ViewModelProvider(
        LocalContext.current as ViewModelStoreOwner
    )[ChatViewModel::class.java]

    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }

    Scaffold (bottomBar = {

        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = "/home", icon = R.drawable.ic_home),
                    NavItem(route = "/stats", icon = R.drawable.ic_stats),
                    NavItem(route = "/ai", icon = R.drawable.ic_ai)
                )
            )
        }

    }) {
        NavHost(
            navController = navController,
            startDestination = "/home",
            modifier = Modifier.padding(it)) {

            composable(route = "/home") {
                bottomBarVisibility = true
                HomeScreen(navController)
            }

            composable(route = "/add") {
                bottomBarVisibility = false
                AddExpense(navController)
            }

            composable(route = "/stats") {
                bottomBarVisibility = true
                StatsScreen(navController)
            }

            composable(route = "/ai") {
                bottomBarVisibility = true
                ChatScreen(navController, chatViewModel)
            }


        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                          navController.navigate(item.route) {
                              popUpTo(navController.graph.startDestinationId)
                              launchSingleTop = true
                              restoreState = true
                          }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}