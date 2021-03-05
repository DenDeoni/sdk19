package com.test.lifehackstudio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ContainerObjects : Parcelable {

    @Parcelize
    data class Company(
        val id: Long,
        val name: String,
        val img: String
    ): ContainerObjects()

    @Parcelize
    data class CompanyDetail(
        val id: Long,
        val name: String,
        val img: String,
        val description: String,
        val lat: Float,
        val lon: Float,
        val www: String,
        val phone: String
    ): ContainerObjects()

}
