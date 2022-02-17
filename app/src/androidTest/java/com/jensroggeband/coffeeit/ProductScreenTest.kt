package com.jensroggeband.coffeeit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jensroggeband.coffeeit.model.CoffeeMachine
import com.jensroggeband.coffeeit.ui.coffee.StyleView
import com.jensroggeband.coffeeit.ui.theme.CoffeeTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            CoffeeTheme {
                StyleView(
                    coffeeMachine = CoffeeMachine(
                        types = listOf(
                            "Ristretto",
                            "Cappuccino",
                            "Espresso",
                            "Americano",
                            "Latte machiatto",
                        )
                    )
                )
            }
        }
    }

    @Test
    fun allAttributesAreShown() {
        composeTestRule.onNodeWithText("Ristretto").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText("Cappuccino").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText("Espresso").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText("Americano").assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithText("Latte machiatto").assertIsDisplayed().assertHasClickAction()
    }

}