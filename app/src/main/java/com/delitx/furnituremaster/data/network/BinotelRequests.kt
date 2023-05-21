package com.delitx.furnituremaster.data.network

import com.delitx.furnituremaster.SERVER_BASE_URL
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class BinotelRequests : CallRequests {
    private val mClient: OkHttpClient = OkHttpClient.Builder().build()
    override suspend fun makeCall(phone: String): Boolean {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("call")
            .addQueryParameter("number", phone)
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}
