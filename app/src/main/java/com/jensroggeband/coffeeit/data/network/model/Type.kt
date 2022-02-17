package com.jensroggeband.coffeeit.data.network.model

import com.google.gson.annotations.SerializedName

data class Type(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val sizes: List<String>,
    val extras: List<String>
)