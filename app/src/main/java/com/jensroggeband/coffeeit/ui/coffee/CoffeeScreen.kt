package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jensroggeband.coffeeit.R
import com.jensroggeband.coffeeit.model.Selection

@Composable
fun CoffeeScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit) {
    OptionsView(options, onSelectOption)
}

@Composable
fun SizesScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit) {
    OptionsView(options, onSelectOption)
}

@Composable
fun ExtrasScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit) {
    OptionsView(options, onSelectOption)
}

@Composable
fun OverviewScreen(selectedCoffee: Selection, selectedSize: Selection, onClickBrewCoffee: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf(selectedCoffee, selectedSize)) {
                CardView(title = it.name, onClick = {})
            }
        }
        CardView(
            modifier = Modifier.align(Alignment.BottomCenter),
            title = stringResource(id = R.string.button_brew_coffee),
            onClick = onClickBrewCoffee
        )
    }
}

@Composable
fun OptionsView(options: List<Selection>, onClick: (Selection) -> Unit) {
    LazyColumn(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) {
            CardView(title = it.name, onClick = { onClick(it) })
        }
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