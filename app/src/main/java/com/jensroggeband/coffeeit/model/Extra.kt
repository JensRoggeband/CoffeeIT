package com.jensroggeband.coffeeit.model

class Extra(
    name: String,
    selectedSubOption: ExtraSelection? = null,
    subSelections: List<ExtraSelection>,
): Selection(name, selectedSubOption, subSelections)