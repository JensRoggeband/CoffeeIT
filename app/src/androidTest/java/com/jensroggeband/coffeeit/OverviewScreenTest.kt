package com.jensroggeband.coffeeit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.jensroggeband.coffeeit.ui.theme.CoffeeTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            CoffeeTheme {
                Overview(
                    onNavigate = {}
                )
            }
        }
    }

    @Test
    fun allItemsAreShown() {
        composeTestRule.onNodeWithText("Tap the machine to start").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start!").assertIsDisplayed().assertHasClickAction()
    }

}