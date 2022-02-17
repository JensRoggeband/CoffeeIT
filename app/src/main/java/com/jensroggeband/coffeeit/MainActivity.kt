package com.jensroggeband.coffeeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.jensroggeband.coffeeit.ui.coffee.CoffeeScreen
import com.jensroggeband.coffeeit.ui.theme.CoffeeTheme
import com.jensroggeband.coffeeit.viewmodel.CoffeeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                CoffeeTheme {
                    MainScreen(coffeeViewModel = hiltViewModel())
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalMaterial3Api
    @Composable
    fun MainScreen(
        modifier: Modifier = Modifier,
        coffeeViewModel: CoffeeViewModel
    ) {
        val navController = rememberNavController()
        Scaffold(
            modifier = modifier,
            topBar = {
                SmallTopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) }
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavMain(
                        navController = navController,
                        coffeeViewModel = coffeeViewModel
                    )
                }
            }
        )
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalMaterial3Api
    @Composable
    fun NavMain(navController: NavHostController, coffeeViewModel: CoffeeViewModel) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                Overview {
                    navController.navigate("coffee")
                }
            }
            composable(Screen.Coffee.route) {
                CoffeeScreen(viewModel = coffeeViewModel)
            }
        }
    }
}

@Composable
fun Overview(onNavigate: () -> Unit) {
    Column {
        Text(text = "Tap the machine to start")
        Button(onClick = onNavigate) {
            Text(text = "Start!")
        }
    }
}

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Home : Screen(route = "home", title = R.string.navigation_screen_home)
    object Coffee : Screen(route = "coffee", title = R.string.navigation_screen_coffee)
}