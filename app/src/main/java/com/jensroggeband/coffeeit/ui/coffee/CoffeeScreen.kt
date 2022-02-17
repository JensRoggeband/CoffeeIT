package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jensroggeband.coffeeit.R
import com.jensroggeband.coffeeit.model.CoffeeMachine
import com.jensroggeband.coffeeit.ui.EmptyScreen
import com.jensroggeband.coffeeit.ui.LoadingScreen
import com.jensroggeband.coffeeit.viewmodel.CoffeeUIState
import com.jensroggeband.coffeeit.viewmodel.CoffeeViewModel

@ExperimentalFoundationApi
@Composable
fun CoffeeScreen(
    modifier: Modifier = Modifier,
    viewModel: CoffeeViewModel,
    productId: String = "60ba1ab72e35f2d9c786c610"
) {

    LaunchedEffect(productId) {
        viewModel.fetchMachine(productId)
    }

    LoadingView(
        modifier = modifier,
        coffeeUIState = viewModel.coffeeUiState
    )
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    coffeeUIState: CoffeeUIState
) {
    Crossfade(targetState = coffeeUIState, modifier = modifier) { currentUiState ->
        when {
            currentUiState.machine != null -> {
                StyleView(coffeeMachine = currentUiState.machine)
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

@Composable
fun StyleView(coffeeMachine: CoffeeMachine) {
    LazyColumn {
        items(coffeeMachine.types) {
            CardView(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardView(title: String) {
    Card(
        modifier = Modifier.clickable(onClick = {}),
        content = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = title,
                maxLines = 2,
            )
        }
    )
}