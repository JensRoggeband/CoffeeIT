package com.jensroggeband.coffeeit.model

abstract class Selection(
    val name: String,
//    val icon: Int,
    val subSelections: List<Selection> = emptyList()
)