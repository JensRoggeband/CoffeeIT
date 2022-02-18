package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jensroggeband.coffeeit.R
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.ui.EmptyScreen
import com.jensroggeband.coffeeit.ui.LoadingScreen
import com.jensroggeband.coffeeit.viewmodel.CoffeeUIState
import com.jensroggeband.coffeeit.viewmodel.CoffeeViewModel

@ExperimentalFoundationApi
@Composable
fun CoffeeScreen(
    modifier: Modifier = Modifier,
    viewModel: CoffeeViewModel,
    machineId: String = "60ba1ab72e35f2d9c786c610"
) {

    LaunchedEffect(machineId) {
        viewModel.fetchMachine(machineId)
    }

    LoadingView(
        modifier = modifier,
        coffeeUIState = viewModel.coffeeUiState,
        viewModel = viewModel
    )
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    coffeeUIState: CoffeeUIState,
    viewModel: CoffeeViewModel
) {
    Crossfade(targetState = coffeeUIState, modifier = modifier) { currentUiState ->
        when {
            currentUiState.machine != null -> {
                OptionsView(options = currentUiState.machine.types) {

                }
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
fun OptionsView(options: List<Selection>, onClick: () -> Unit) {
    LazyColumn(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) {
            CardView(title = it.name, onClick = onClick)
        }
    }
}

@Composable
fun OverviewView(options: List<Selection>, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) {
                CardView(title = it.name, onClick = onClick)
            }
        }
        CardView(
            modifier = Modifier.align(Alignment.BottomCenter),
            title = stringResource(id = R.string.button_brew_coffee),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardView(modifier: Modifier = Modifier, title: String, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = title,
                maxLines = 2,
            )
        }
    )
}