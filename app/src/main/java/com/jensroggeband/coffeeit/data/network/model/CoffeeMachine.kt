package com.jensroggeband.coffeeit.data.network.model

data class CoffeeMachine(
    val types: List<Type>,
    val sizes: List<Size>,
    val extras: List<Extra>
)

fun CoffeeMachine.toModel() = com.jensroggeband.coffeeit.model.CoffeeMachine(
    types = types.map { it.name }
)

