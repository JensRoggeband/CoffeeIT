package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jensroggeband.coffeeit.R
import com.jensroggeband.coffeeit.model.Extra
import com.jensroggeband.coffeeit.model.ExtraSelection
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.viewmodel.CoffeeUIState

@Composable
fun CoffeeScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit) {
    OptionsView(options, onSelectOption)
}

@Composable
fun SizesScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit) {
    OptionsView(options, onSelectOption)
}

@Composable
fun ExtrasScreen(options: List<Selection>, onSelectOption: (Selection) -> Unit, onNavigate: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        ExpandableOptionsView(options, onSelectOption)
        CardView(
            modifier = Modifier.align(Alignment.BottomCenter),
            title = stringResource(id = R.string.button_confirm),
            onClick = onNavigate
        )
    }
}

@Composable
fun OverviewScreen(uiState: CoffeeUIState, onClickBrewCoffee: () -> Unit) {
    val selectedItems = listOfNotNull(uiState.selectedCoffee, uiState.selectedSize, uiState.selectedExtras)
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        LazyColumn(modifier = Modifier.align(Alignment.TopCenter), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedItems) {
                val selectedOption: Selection? = it.selectedOption
                if (selectedOption != null) {
                    ExpandableCardView(title = it.name, selection = it, subSelections = listOf(selectedOption), onClick = {}, defaultExpanded = true)
                }
                else {
                    CardView(title = it.name, onClick = {})
                }
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

@Composable
fun ExpandableOptionsView(options: List<Selection>, onClick: (Selection) -> Unit) {
    LazyColumn(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { selection ->
            ExpandableCardView(title = selection.name, selection = selection, subSelections = selection.subSelections, onClick = { onClick(it) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCardView(
    modifier: Modifier = Modifier,
    title: String, selection: Selection,
    subSelections: List<Selection>,
    onClick: (Selection) -> Unit,
    defaultExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(defaultExpanded) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(subSelections[0]) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                isExpanded = !isExpanded
                onClick(selection)
            }),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = title,
                maxLines = 2,
            )
            if (isExpanded) {
                subSelections.forEach {
                    Row {
                        Text(text = it.name)
                        RadioButton(selected = (it == selectedOption), onClick = {
                            onOptionSelected(it)
                            onClick(Extra(name = selection.name, selectedSubOption = ExtraSelection(it.name), subSelections = listOf()))
                        })
                    }
                }
            }
        }
    )
}