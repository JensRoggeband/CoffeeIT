package com.jensroggeband.coffeeit

import com.jensroggeband.coffeeit.data.CoffeeRepository
import com.jensroggeband.coffeeit.data.network.model.CoffeeMachine
import com.jensroggeband.coffeeit.data.network.model.Type
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
        coEvery { repository.getMachine(any()) } returns Response.success(CoffeeMachine(
            types = listOf(
                Type(
                    id = "123",
                    name = "Ristretto",
                    sizes = listOf(),
                    extras = listOf()
                )
            ),
            sizes = listOf(),
            extras = listOf()
        ))

        assertNull(viewModel.coffeeUiState.machine)

        viewModel.fetchMachine("123")

        assertEquals(com.jensroggeband.coffeeit.model.CoffeeMachine(
            types = listOf("Ristretto")
        ), viewModel.coffeeUiState.machine)
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

}