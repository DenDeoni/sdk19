package com.test.sdk19.network

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object Networking {

    private val client = OkHttpClient()

    fun getCompaniesCall(): Call {

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("www.lifehack.studio")
            .addPathSegments("test_task/test.php")
            .build()

        val request = Request.Builder()
            .url(url)
            .build()
        return client.newCall(request)
    }

    fun getCompanyCall(idCompany: Long): Call {

        val request = Request.Builder()
            .url("https://lifehack.studio/test_task/test.php?id=$idCompany")
            .build()
        return client.newCall(request)
    }

}