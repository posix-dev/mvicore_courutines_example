package com.shepeliev.livemvi.data

interface CalculatorApi {
    suspend fun add(a: Int, b: Int): Int
}
