package com.jensroggeband.coffeeit

import com.jensroggeband.coffeeit.data.CoffeeRepository
import com.jensroggeband.coffeeit.data.network.model.*
import com.jensroggeband.coffeeit.viewmodel.CoffeeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response

@ExperimentalCoroutinesApi
class CoffeeViewModelTest {

    private lateinit var viewModel: CoffeeViewModel

    @MockK
    private lateinit var repository: CoffeeRepository

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = CoffeeViewModel(repository)
    }

    @Test
    fun productUiStateIsCorrectlyUpdatedAfterSuccess() {
        coEvery { repository.getMachine(any()) } returns Response.success(coffeeMachine())

        assertNull(viewModel.coffeeUiState.machine)

        viewModel.fetchMachine("123")

        assertEquals(1, viewModel.coffeeUiState.machine?.types?.size)
        assertEquals("Ristretto", viewModel.coffeeUiState.machine?.types?.get(0)?.name)

        assertEquals(2, viewModel.coffeeUiState.machine?.types?.get(0)?.sizes?.size)
        assertEquals("Large", viewModel.coffeeUiState.machine?.types?.get(0)?.sizes?.get(0)?.name)
        assertEquals("Venti", viewModel.coffeeUiState.machine?.types?.get(0)?.sizes?.get(1)?.name)

        assertEquals(1, viewModel.coffeeUiState.machine?.types?.get(0)?.extras?.size)
        assertEquals("Select the amount of sugar", viewModel.coffeeUiState.machine?.types?.get(0)?.extras?.get(0)?.name)
    }

    @Test
    fun productUiStateIsCorrectlyUpdatedAfterError() {
        coEvery { repository.getMachine(any()) } returns Response.error(500,
            "Server error".toResponseBody("application/json".toMediaTypeOrNull())
        )

        assertNull(viewModel.coffeeUiState.machine)

        viewModel.fetchMachine("123")

        assertNull(viewModel.coffeeUiState.machine)
        assertTrue(viewModel.coffeeUiState.throwError)
    }

    private fun coffeeMachine() = CoffeeMachine(
        types = listOf(
            Type(
                id = "123",
                name = "Ristretto",
                sizes = listOf(
                    "4",
                    "5"
                ),
                extras = listOf(
                    "7"
                )
            )
        ),
        sizes = listOf(
            Size(
                id = "4",
                name = "Large"
            ),
            Size(
                id = "5",
                name = "Venti"
            ),
            Size(
                id = "6",
                name = "Tall"
            )
        ),
        extras = listOf(
            Extra(
                id = "7",
                name = "Select the amount of sugar",
                subSelections = listOf(
                    SubSelection(
                        id = "10",
                        name = "A lot"
                    ),
                    SubSelection(
                        id = "11",
                        name = "Normal"
                    )
                )
            )
        )
    )
}