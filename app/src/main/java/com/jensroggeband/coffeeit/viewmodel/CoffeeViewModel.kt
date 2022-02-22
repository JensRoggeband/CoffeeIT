package com.jensroggeband.coffeeit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensroggeband.coffeeit.data.CoffeeRepository
import com.jensroggeband.coffeeit.data.network.model.toModel
import com.jensroggeband.coffeeit.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeViewModel @Inject constructor(
    private val repository: CoffeeRepository
) : ViewModel() {

    var coffeeUiState by mutableStateOf(CoffeeUIState())
        private set

    fun fetchMachine(id: String) {
        coffeeUiState = CoffeeUIState(isLoading = true)
        viewModelScope.launch {
            repository.getMachine(id).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        coffeeUiState = CoffeeUIState(machine = it.toModel())
                    }
                }
                else {
                    coffeeUiState = CoffeeUIState(throwError = true)
                }
            }
        }
    }

    fun resetSizes() {
        coffeeUiState = coffeeUiState.copy(selectedSize = null)
    }

    fun resetExtras() {
        coffeeUiState = coffeeUiState.copy(selectedExtras = emptyList())
    }

    fun selectCoffee(selection: Selection) {
        val coffee = if (selection is Coffee) selection else return
        coffeeUiState = coffeeUiState.copy(selectedCoffee = coffee)
    }

    fun selectSize(selection: Selection) {
        val size = if (selection is Size) selection else return
        coffeeUiState = coffeeUiState.copy(selectedSize = size)
    }

    fun selectExtras(selection: Selection, isSelected: Boolean) {
        val extras = if (selection is Extra) selection else return
        val newExtras = coffeeUiState.selectedExtras.toMutableList()
        if (isSelected) {
            newExtras.add(extras)
        }
        else {
            newExtras.removeAll { it.name == extras.name }
        }
        coffeeUiState = coffeeUiState.copy(selectedExtras = newExtras)
    }

    fun changeExtras(selection: Selection) {
        val extras = if (selection is Extra) selection else return
        val newExtras = coffeeUiState.selectedExtras.toMutableList()
        val item = newExtras.find { it.name == extras.name }
        newExtras[newExtras.indexOf(item)] = extras
        coffeeUiState = coffeeUiState.copy(selectedExtras = newExtras)
    }

}

data class CoffeeUIState(
    val machine: CoffeeMachine? = null,
    val selectedCoffee: Coffee? = null,
    val selectedSize: Size? = null,
    val selectedExtras: List<Extra> = listOf(),
    val isLoading: Boolean = false,
    val throwError: Boolean = false
)