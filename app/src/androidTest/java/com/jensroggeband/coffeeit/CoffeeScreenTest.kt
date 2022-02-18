package com.jensroggeband.coffeeit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jensroggeband.coffeeit.model.Coffee
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.ui.coffee.OptionsView
import com.jensroggeband.coffeeit.ui.theme.CoffeeTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoffeeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            CoffeeTheme {
                OptionsView(
                    options = listOf(
                        Coffee("Ristretto", listOf(), listOf()),
                        Coffee("Cappuccino", listOf(), listOf()),
                        Coffee("Espresso", listOf(), listOf()),
                        Coffee("Americano", listOf(), listOf()),
                        Coffee("Latte machiatto", listOf(), listOf()),
                    ),
                    onClick = {}
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