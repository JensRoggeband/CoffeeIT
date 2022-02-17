package com.jensroggeband.coffeeit.data.network

import com.jensroggeband.coffeeit.data.network.model.CoffeeMachine
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("coffee-machine/{id}")
    suspend fun getMachine(@Path("id") machineId: String): Response<CoffeeMachine>

}