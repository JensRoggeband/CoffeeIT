package com.jensroggeband.coffeeit.data.network.model

import com.google.gson.annotations.SerializedName

data class Size(
    @SerializedName("_id")
    val id: String,
    val name: String
)