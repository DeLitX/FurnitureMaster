package com.delitx.furnituremaster.data.network

interface CallRequests {
    suspend fun makeCall(phone: String): Boolean
}
