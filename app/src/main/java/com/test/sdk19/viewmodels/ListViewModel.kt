package com.test.sdk19.viewmodels

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.sdk19.ContainerObjects
import com.test.sdk19.repository.CompanyDetailRepository
import com.test.sdk19.repository.CompanyListRepository
import com.test.sdk19.utils.SingleLiveEvent
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class ListViewModel : ViewModel(), Parcelable {

    @IgnoredOnParcel
    private val listRepository = CompanyListRepository()
    @IgnoredOnParcel
    private val detailRepository = CompanyDetailRepository()


    @IgnoredOnParcel
    private val companiesListLiveData = MutableLiveData<List<ContainerObjects.Company>>(emptyList())
    val companiesList: LiveData<List<ContainerObjects.Company>>
        get() = companiesListLiveData
    fun loadCompaniesList() {
        listRepository.loadCompanies { companies ->
            companiesListLiveData.postValue(companies)
        }
    }

    @IgnoredOnParcel
    private val companyDetailLiveData = SingleLiveEvent<List<ContainerObjects.CompanyDetail>>()
    val companyDetail: SingleLiveEvent<List<ContainerObjects.CompanyDetail>>
        get() = companyDetailLiveData
    fun loadCompanyDetail(idCompany: Long) {
        detailRepository.loadCompanyDetail(idCompany) { detail ->
            companyDetailLiveData.postValue(detail)
        }
    }


}