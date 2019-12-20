package com.shepeliev.livemvi.data

import com.shepeliev.livemvi.data.CalculatorApi
import kotlinx.coroutines.delay

class RemoteCalculatorApi : CalculatorApi {
    override suspend fun add(a: Int, b: Int): Int {
        if (b > 0) {
            delay(100)
        } else {
            // our calculator does't like subtract, so it do it very slowly :)
            delay(5000)
        }

        return a + b
    }
}
