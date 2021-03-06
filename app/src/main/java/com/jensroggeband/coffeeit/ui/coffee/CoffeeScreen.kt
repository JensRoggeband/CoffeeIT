package com.jensroggeband.coffeeit.ui.coffee

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jensroggeband.coffeeit.R
import com.jensroggeband.coffeeit.Screen
import com.jensroggeband.coffeeit.model.*
import com.jensroggeband.coffeeit.viewmodel.CoffeeUIState

@Composable
fun ExtrasScreen(
    options: List<Selection>,
    onAddOption: (Selection) -> Unit,
    onRemoveOption: (Selection) -> Unit,
    onChangeSubSelection: (Selection) -> Unit,
    onNavigate: () -> Unit
) {
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
fun OverviewScreen(uiState: CoffeeUIState, navController: NavHostController, onClickBrewCoffee: () -> Unit) {
    val selectedItems = listOfNotNull(uiState.selectedCoffee, uiState.selectedSize)
    val selectedExtras = uiState.selectedExtras
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectedItems) {
                    val onClick: () -> Unit = when (it) {
                        is Coffee -> { { navController.navigate(Screen.Coffee.route) { popUpTo(Screen.Home.route) } } }
                        is Size -> { { navController.navigate(Screen.Sizes.route) { popUpTo(Screen.Coffee.route) } } }
                        is Extra -> { { navController.navigate(Screen.Extras.route) { popUpTo(Screen.Sizes.route) } } }
                        else -> { { navController.navigate(Screen.Coffee.route) { popUpTo(Screen.Home.route) } } }
                    }
                    CardView(title = it.name, onClick = onClick, withEditAction = true)
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

@Composable
fun ExpandableOptionsView(options: List<Selection>, onAdd: (Selection) -> Unit, onRemove: (Selection) -> Unit, onChangeSubSelection: (Selection) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
fun CardView(modifier: Modifier = Modifier, title: String, onClick: () -> Unit, withEditAction: Boolean = false) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(16.dp).weight(3f),
                    text = title
                )
                if (withEditAction) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        text = stringResource(id = R.string.button_edit),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

        }
    )
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
                Divider(modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                subSelections.forEachIndexed { index, subSelection ->
                    val onClick = {
                        onOptionSelected(index)
                        onChangeSubSelection(
                            Extra(
                                name = parentSelection.name,
                                selectedSubOption = ExtraSelection(subSelection.name),
                                subSelections = listOf()
                            )
                        )
                    }
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                            .clickable(onClick = onClick),
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(3f),
                                text = subSelection.name,
                            )
                            RadioButton(
                                modifier = Modifier.weight(1f),
                                selected = (selectedOptionIndex == index),
                                onClick = onClick
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}