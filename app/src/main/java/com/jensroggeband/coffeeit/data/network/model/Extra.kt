package com.jensroggeband.coffeeit.data.network.model

import com.google.gson.annotations.SerializedName

data class Extra(
    @SerializedName("_id")
    val id: String,
    val name: String,
    @SerializedName("subselections")
    val subSelections: List<SubSelection>
)

data class SubSelection(
    @SerializedName("_id")
    val id: String,
    val name: String,
)