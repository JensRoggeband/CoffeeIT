package com.jensroggeband.coffeeit.data.network.model

import com.jensroggeband.coffeeit.model.Coffee
import com.jensroggeband.coffeeit.model.ExtraSelection
import com.jensroggeband.coffeeit.model.Selection
import com.jensroggeband.coffeeit.model.Size as SizeModel
import com.jensroggeband.coffeeit.model.Extra as ExtraModel

data class CoffeeMachine(
    val types: List<Type>,
    val sizes: List<Size>,
    val extras: List<Extra>
)

fun CoffeeMachine.toModel() = com.jensroggeband.coffeeit.model.CoffeeMachine(
    types = types.map { it.toModel(sizes, extras) }
)

fun Type.toModel(allSizes: List<Size>, allExtras: List<Extra>) = Coffee(
    name = name,
    sizes = sizes.map { SizeModel(name = allSizes.find { size -> size.id == it }?.name ?: "") },
    extras = extras.map {
        val extra = allExtras.find { extra -> extra.id == it }
        ExtraModel(
            name = extra?.name ?: "",
            subSelections = extra?.subSelections?.map { ExtraSelection(name = it.name) } ?: listOf()
        )
    }
)
