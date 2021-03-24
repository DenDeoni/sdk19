package com.test.sdk19.repository

import com.test.sdk19.ContainerObjects
import com.test.sdk19.MAIN_PATH
import com.test.sdk19.network.Networking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class CompanyDetailRepository {

    fun loadCompanyDetail(
        idCompany: Long,
        callback: (List<ContainerObjects.CompanyDetail>) -> Unit
    ) {
        Networking.getCompanyCall(idCompany).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.string()
                        ?.trim()
                        ?.replace(". \"", ".\"")
                    ?.replace(" \"", " «")
                        ?.replace("\" ", "» ")
                        .orEmpty()
                    val company = parseServerResponse(responseBodyString)
                    callback(company)
                } else {
                    callback(emptyList())
                }
            }

        })
    }

    private fun parseServerResponse(responseBodyString: String): List<ContainerObjects.CompanyDetail> {
        return try {
            val companyJsonArray = JSONArray(responseBodyString)
            val companyJsonObject = companyJsonArray.getJSONObject(0)
            val id = companyJsonObject.getString("id").toLong()
            val name = companyJsonObject.getString("name")
            val img = MAIN_PATH + companyJsonObject.getString("img")
            val description = companyJsonObject.getString("description")
            val lat = companyJsonObject.getString("lat").toFloat()
            val lon = companyJsonObject.getString("lon").toFloat()
            val www = companyJsonObject.getString("www")
            val phone = companyJsonObject.getString("phone")
            listOf(
                ContainerObjects.CompanyDetail(
                    id = id, name = name, img = img,
                    description = description,
                    lat = lat, lon = lon, www = www, phone = phone
                )
            )
        } catch (e: JSONException) {
            println("e: $e")
            emptyList()
        }
    }

}