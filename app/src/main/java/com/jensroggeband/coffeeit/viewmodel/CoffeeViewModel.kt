package com.jensroggeband.coffeeit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensroggeband.coffeeit.data.CoffeeRepository
import com.jensroggeband.coffeeit.data.network.model.toModel
import com.jensroggeband.coffeeit.model.Coffee
import com.jensroggeband.coffeeit.model.CoffeeMachine
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.model.Size
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

    fun selectCoffee(selection: Selection) {
        val coffee = if (selection is Coffee) selection else return
        coffeeUiState = coffeeUiState.copy(selectedCoffee = coffee)
    }

    fun selectSize(size: Selection) {
        coffeeUiState = coffeeUiState.copy(selectedSize = Size(size.name))
    }

}

data class CoffeeUIState(
    val machine: CoffeeMachine? = null,
    val selectedCoffee: Coffee? = null,
    val selectedSize: Size? = null,
    val isLoading: Boolean = false,
    val throwError: Boolean = false
)