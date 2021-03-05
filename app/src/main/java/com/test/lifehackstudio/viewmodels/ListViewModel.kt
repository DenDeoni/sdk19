package com.test.lifehackstudio.viewmodels

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.lifehackstudio.ContainerObjects
import com.test.lifehackstudio.repository.CompanyDetailRepository
import com.test.lifehackstudio.repository.CompanyListRepository
import com.test.lifehackstudio.utils.SingleLiveEvent
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