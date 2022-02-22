package com.jensroggeband.coffeeit.model

abstract class Selection(
    val name: String,
    val selectedOption: Selection? = null,
    val subSelections: List<Selection> = emptyList()
)