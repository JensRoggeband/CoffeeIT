package com.jensroggeband.coffeeit.data

import com.jensroggeband.coffeeit.data.network.ApiService
import javax.inject.Inject

class CoffeeRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun getMachine(machineId: String) = apiService.getMachine(machineId)

}