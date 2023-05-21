package com.delitx.furnituremaster.data.network

import android.net.Uri
import com.delitx.furnituremaster.SERVER_BASE_URL
import com.delitx.furnituremaster.data.network.exceptions.InternetConnectionErrorException
import com.delitx.furnituremaster.data_models.CommentedImage
import com.delitx.furnituremaster.data_models.MapMarker
import com.delitx.furnituremaster.data_models.PropertyValue
import com.delitx.furnituremaster.data_models.dtos.ProductDTO
import com.delitx.furnituremaster.data_models.dtos.ProductPropertyDTO
import com.delitx.furnituremaster.data_models.network_dtos.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ServerRequestsImpl : ServerRequests {
    private val mStorage = FirebaseStorage.getInstance()
    private val mClient: OkHttpClient = OkHttpClient.Builder().build()
    private val JSON = ".json"

    init {
        Firebase.remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 1
            }
        )
    }

    override fun downloadFileLink(link: String, file: File, onSuccess: (link: Uri) -> Unit) {
        mStorage.getReference(link).downloadUrl.addOnSuccessListener {
            onSuccess(it)
        }.addOnFailureListener {
            if (true) {
            }
        }
    }

    override fun downloadFile(link: String, file: File, onSuccess: () -> Unit) {
        mStorage.getReference(link).getFile(file).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            if (true) {
            }
        }
    }

    override suspend fun getMapMarkers(): List<MapMarker> {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("map_markers$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return if (json != null) {
                    getMapMarkersListFromJSON(json)
                } else {
                    listOf()
                }
            }
        } catch (e: Exception) {
            return listOf()
        }
        return listOf()
    }

    private fun getMapMarkersListFromJSON(string: String): List<MapMarker> {
        val result = mutableListOf<MapMarker>()
        val json = JSONArray(string)
        for (i in 0 until json.length()) {
            val value = json[i]
            if (value is JSONObject) {
                val gson = GsonBuilder().create()
                val marker = gson.fromJson(value.toString(), MapMarkerNetworkDTO::class.java)
                result.add(marker.toModel())
            }
        }
        return result
    }

    override suspend fun getAllProducts(): List<ProductDTO> {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("products$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return if (json != null) {
                    getProductsListFromJSON(json)
                } else {
                    listOf()
                }
            }
        } catch (e: Exception) {
            return listOf()
        }
        return listOf()
    }

    override suspend fun getAllImages(): List<CommentedImage> {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("commented_images$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return if (json != null) {
                    getImagesFromJSON(json)
                } else {
                    listOf()
                }
            }
        } catch (e: Exception) {
            return listOf()
        }
        return listOf()
    }

    override suspend fun getAllProperties(): List<ProductPropertyDTO> {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("properties$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return if (json != null) {
                    getPropertiesFromJSON(json)
                } else {
                    listOf()
                }
            }
        } catch (e: Exception) {
            return listOf()
        }
        return listOf()
    }

    override suspend fun getAllVariations(): List<PropertyValue> {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("values$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return if (json != null) {
                    getVariationsFromJSON(json)
                } else {
                    listOf()
                }
            }
        } catch (e: Exception) {
            return listOf()
        }
        return listOf()
    }

    private fun getImagesFromJSON(string: String): List<CommentedImage> {
        val result = mutableListOf<CommentedImage>()
        val json = JSONArray(string)
        for (i in 0 until json.length()) {
            val value = json[i]
            if (value is JSONObject) {
                result.add(commentedImageFromJSON(value))
            }
        }
        return result
    }

    private fun getPropertiesFromJSON(string: String): List<ProductPropertyDTO> {
        val result = mutableListOf<ProductPropertyDTO>()
        val json = JSONArray(string)
        for (i in 0 until json.length()) {
            val value = json[i]
            if (value is JSONObject) {
                result.add(propertyFromJSON(value))
            }
        }
        return result
    }

    private fun getVariationsFromJSON(string: String): List<PropertyValue> {
        val result = mutableListOf<PropertyValue>()
        val json = JSONArray(string)
        for (i in 0 until json.length()) {
            val value = json[i]
            if (value is JSONObject) {
                result.add(variationFromJson(value))
            }
        }
        return result
    }

    private fun getProductsListFromJSON(string: String): List<ProductDTO> {
        val json = JSONArray(string)
        val result = mutableListOf<ProductDTO>()
        for (i in 0 until json.length()) {
            val value = json[i]
            if (value is JSONObject) {
                val gson = GsonBuilder().create()
                val product = gson.fromJson(value.toString(), ProductNetworkDto::class.java)
                result.add(product.toProductDto())
            }
        }
        return result
    }

    private fun commentedImageFromJSON(json: JSONObject): CommentedImage {
        val gson = GsonBuilder().create()
        val value = gson.fromJson(json.toString(), CommentedImageNetworkDto::class.java)
        return value.toModel()
    }

    private fun propertyFromJSON(json: JSONObject): ProductPropertyDTO {
        val gson = GsonBuilder().create()
        val value = gson.fromJson(json.toString(), ProductPropertyNetworkDto::class.java)
        return value.toProductPropertyDTO()
    }

    private fun variationFromJson(json: JSONObject): PropertyValue {
        return try {
            val gson = GsonBuilder().create()
            val value = gson.fromJson(json.toString(), NumeralValueNetworkDto::class.java)
            value.toModel()
        } catch (e: Exception) {
            val gson = GsonBuilder().create()
            val value = gson.fromJson(json.toString(), NamedValueNetworkDto::class.java)
            value.toModel()
        }
    }

    override suspend fun markProductAddedToBasket(productId: String) {
        TODO("Not yet implemented")
    }

    override fun getTimestamp(): Long {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("timestamp$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return json?.toLong() ?: 0
            } else {
                throw InternetConnectionErrorException()
            }
        } catch (e: InternetConnectionErrorException) {
            throw e
        } catch (e: Exception) {
            throw InternetConnectionErrorException()
        }
    }

    override fun getAndroidVersion(): Long {
        val url = HttpUrl.Builder().scheme("https")
            .host(SERVER_BASE_URL)
            .addPathSegment("android_version$JSON")
            .build()
        val request = Request.Builder().url(url).build()
        val call = mClient.newCall(request)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                return json?.toLong() ?: 0
            } else {
                throw InternetConnectionErrorException()
            }
        } catch (e: InternetConnectionErrorException) {
            throw e
        } catch (e: Exception) {
            throw InternetConnectionErrorException()
        }
    }
}
