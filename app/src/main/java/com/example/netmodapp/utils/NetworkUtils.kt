package com.example.netmodapp.utils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkUtils {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun makeRequest(
        url: String,
        method: String,
        headers: List<com.example.netmodapp.models.Header>,
        body: String?,
        callback: (ResponseData) -> Unit
    ) {
        val requestBuilder = Request.Builder().url(url)

        // Add headers
        headers.forEach { header ->
            if (header.key.isNotEmpty() && header.value.isNotEmpty()) {
                requestBuilder.addHeader(header.key, header.value)
            }
        }

        // Set request method with optional body
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = if (body?.isNotEmpty() == true) body.toRequestBody(mediaType) else null

        val request = when (method.uppercase()) {
            "GET" -> requestBuilder.get().build()
            "POST" -> requestBuilder.post(requestBody ?: RequestBody.create(null, byteArrayOf())).build()
            "PUT" -> requestBuilder.put(requestBody ?: RequestBody.create(null, byteArrayOf())).build()
            "DELETE" -> requestBuilder.delete(requestBody).build()
            else -> requestBuilder.get().build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(ResponseData(false, e.message ?: "Request failed", -1, -1))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseTime = System.currentTimeMillis()
                val responseBody = response.body?.string() ?: ""
                val responseSize = responseBody.length
                val responseStatus = response.code
                val success = response.isSuccessful

                callback(
                    ResponseData(
                        success = success,
                        data = responseBody,
                        status = responseStatus,
                        size = responseSize
                    )
                )
            }
        })
    }

    data class ResponseData(
        val success: Boolean,
        val data: String,
        val status: Int,
        val size: Int
    )
}