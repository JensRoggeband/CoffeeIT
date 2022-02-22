package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
fun ExtrasScreen(options: List<Selection>, onAddOption: (Selection) -> Unit, onRemoveOption: (Selection) -> Unit, onChangeSubSelection: (Selection) -> Unit, onNavigate: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        ExpandableOptionsView(options, onAddOption, onRemoveOption, onChangeSubSelection)
        CardView(
            modifier = Modifier.align(Alignment.BottomCenter),
            title = stringResource(id = R.string.button_confirm),
            onClick = onNavigate
        )
    }
}

@Composable
fun OverviewScreen(uiState: CoffeeUIState, onClickBrewCoffee: () -> Unit) {
    val selectedItems = listOfNotNull(uiState.selectedCoffee, uiState.selectedSize)
    val selectedExtras = uiState.selectedExtras
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectedItems) {
                    CardView(title = it.name, onClick = {})
                }
            }
            if (selectedExtras.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedExtras) {
                        val selectedOption: Selection? = it.selectedOption
                        if (selectedOption != null) {
                            ExpandableCardView(
                                title = it.name,
                                parentSelection = it,
                                subSelections = listOf(selectedOption),
                                onAdd = {},
                                onRemove = {},
                                onChangeSubSelection = {}
                            )
                        }
                    }
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
fun ExpandableOptionsView(options: List<Selection>, onAdd: (Selection) -> Unit, onRemove: (Selection) -> Unit, onChangeSubSelection: (Selection) -> Unit) {
    LazyColumn(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { selection ->
            ExpandableCardView(
                title = selection.name,
                parentSelection = selection,
                subSelections = selection.subSelections,
                onAdd = onAdd,
                onRemove = onRemove,
                onChangeSubSelection = onChangeSubSelection
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCardView(
    modifier: Modifier = Modifier,
    title: String,
    parentSelection: Selection,
    subSelections: List<Selection>,
    onAdd: (Selection) -> Unit,
    onRemove: (Selection) -> Unit,
    onChangeSubSelection: (Selection) -> Unit
) {
    val expanded = parentSelection.selectedOption != null
    var isExpanded by rememberSaveable { mutableStateOf(expanded) }
    val defaultIndex = if (parentSelection.selectedOption != null) subSelections.indexOf(parentSelection.selectedOption) else 0
    val (selectedOptionIndex, onOptionSelected) = rememberSaveable { mutableStateOf(defaultIndex) }
    val selectedOption = subSelections[selectedOptionIndex]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                isExpanded = !isExpanded
                val option = Extra(
                    name = parentSelection.name,
                    selectedSubOption = ExtraSelection(selectedOption.name),
                    subSelections = listOf()
                )
                if (isExpanded) onAdd(option) else onRemove(option)
            }),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Text(
                modifier = Modifier.padding(16.dp),
                text = title,
                maxLines = 2,
            )
            if (isExpanded) {
                subSelections.forEachIndexed { index, subSelection ->
                    Row {
                        Text(text = subSelection.name)
                        RadioButton(
                            selected = (selectedOptionIndex == index),
                            onClick = {
                                onOptionSelected(index)
                                onChangeSubSelection(
                                    Extra(
                                        name = parentSelection.name,
                                        selectedSubOption = ExtraSelection(subSelection.name),
                                        subSelections = listOf()
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}