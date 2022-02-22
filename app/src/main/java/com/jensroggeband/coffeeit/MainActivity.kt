package com.jensroggeband.coffeeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.ui.EmptyScreen
import com.jensroggeband.coffeeit.ui.LoadingScreen
import com.jensroggeband.coffeeit.ui.coffee.*
import com.jensroggeband.coffeeit.ui.theme.CoffeeTheme
import com.jensroggeband.coffeeit.viewmodel.CoffeeUIState
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
                Overview(
                    onNavigate = { navController.navigate(Screen.Coffee.route) }
                )
            }
            composable(Screen.Coffee.route) {
                StartScreen(
                    viewModel = coffeeViewModel,
                    onSelectOption = {
                        coffeeViewModel.selectCoffee(it)
                        navController.navigate(Screen.Sizes.route)
                    }
                )
            }
            composable(Screen.Sizes.route) {
                OptionsView(
                    options = coffeeViewModel.coffeeUiState.selectedCoffee?.sizes ?: listOf(),
                    onClick = {
                        coffeeViewModel.selectSize(it)
                        navController.navigate(Screen.Extras.route)
                    }
                )
            }
            composable(Screen.Extras.route) {
                ExtrasScreen(
                    options = coffeeViewModel.coffeeUiState.selectedCoffee?.extras ?: listOf(),
                    onAddOption = { coffeeViewModel.selectExtras(it, true) },
                    onRemoveOption = { coffeeViewModel.selectExtras(it, false) },
                    onChangeSubSelection = { coffeeViewModel.changeExtras(it) },
                    onNavigate = { navController.navigate(Screen.Overview.route) }
                )
            }
            composable(Screen.Overview.route) {
                OverviewScreen(
                    uiState = coffeeViewModel.coffeeUiState,
                    onClickBrewCoffee = { /* Confirm selections in API and brew the coffee! */ }
                )
            }
        }
    }
}

@Composable
fun Overview(onNavigate: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Tap the machine to start")
        Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = onNavigate) {
            Text(text = "Start!")
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    viewModel: CoffeeViewModel,
    onSelectOption: (Selection) -> Unit,
    machineId: String = "60ba1ab72e35f2d9c786c610"
) {

    LaunchedEffect(machineId) {
        viewModel.fetchMachine(machineId)
    }

    LoadingView(
        modifier = modifier,
        coffeeUIState = viewModel.coffeeUiState,
        viewModel = viewModel,
        onSelectOption = onSelectOption
    )
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    coffeeUIState: CoffeeUIState,
    viewModel: CoffeeViewModel,
    onSelectOption: (Selection) -> Unit
) {
    Crossfade(targetState = coffeeUIState, modifier = modifier) { currentUiState ->
        when {
            currentUiState.machine != null -> {
                OptionsView(
                    options = viewModel.coffeeUiState.machine?.types ?: listOf(),
                    onClick = onSelectOption
                )
            }
            currentUiState.isLoading -> {
                LoadingScreen()
            }
            else -> {
                EmptyScreen(stringResource(id = R.string.screen_message_error_state))
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Home : Screen(route = "home", title = R.string.navigation_screen_home)
    object Coffee : Screen(route = "coffee", title = R.string.navigation_screen_coffee)
    object Sizes : Screen(route = "sizes", title = R.string.navigation_screen_size)
    object Extras : Screen(route = "extras", title = R.string.navigation_screen_extras)
    object Overview : Screen(route = "overview", title = R.string.navigation_screen_overview)
}