package com.jensroggeband.coffeeit.model

class Coffee(
    name: String,
    val sizes: List<Size>,
    val extras: List<Extra>
): Selection(name)