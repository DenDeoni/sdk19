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

class CompanyListRepository {

    fun loadCompanies(callback: (List<ContainerObjects.Company>) -> Unit) {
        Networking.getCompaniesCall().enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.string().orEmpty()
                    val companies = parseServerResponse(responseBodyString)
                    callback(companies)
                } else {
                    callback(emptyList())
                }
            }

        })
    }

    private fun parseServerResponse(responseBodyString: String): List<ContainerObjects.Company> {
        return try {
            val companiesArray = JSONArray(responseBodyString)
            (0 until companiesArray.length())
                .map { index -> companiesArray.getJSONObject(index) }
                .map { companyJsonObject ->
                    val id = companyJsonObject.getString("id").toLong()
                    val name = companyJsonObject.getString("name")
                    val img = MAIN_PATH + companyJsonObject.getString("img")
                    ContainerObjects.Company(id = id, name = name, img = img)
                }
        } catch (e: JSONException) {
            emptyList()
        }
    }

}