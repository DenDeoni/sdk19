package com.test.sdk19.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.sdk19.*
import com.test.sdk19.adapter.CommonAdapter
import com.test.sdk19.databinding.FragmentCompanyListBinding
import com.test.sdk19.viewbindingdelegate.viewBinding
import com.test.sdk19.viewmodels.ListViewModel


class CompanyListFragment : Fragment(R.layout.fragment_company_list) {

    private val binding by viewBinding<FragmentCompanyListBinding>()
    private val listViewModel: ListViewModel by viewModels()
    private var commonAdapter: CommonAdapter? = null
    private lateinit var companyDetailDialog: CompanyDetailDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList(emptyList())
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        commonAdapter = null
    }

    private fun initList(list: List<ContainerObjects.Company>) {
        commonAdapter = CommonAdapter { idItem, _, _ ->
            showDialog(idItem)
        }
        commonAdapter?.updateObjectsList(list)
        with(binding.objectsList) {
            adapter = commonAdapter
            layoutManager = setLayout()
            setHasFixedSize(false)
        }
    }

    private fun bindViewModel() {
        listViewModel.companiesList.observe(viewLifecycleOwner) {
            commonAdapter?.items = it
        }
        listViewModel.loadCompaniesList()
    }

    private fun showDialog(idItem: Long) {
        companyDetailDialog = CompanyDetailDialog.newInstance(listViewModel, idItem)
        companyDetailDialog.show(childFragmentManager, COMPANY_DETAIL)
    }

    private fun setLayout(): RecyclerView.LayoutManager {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(context).apply {
                LinearLayoutManager.VERTICAL
            }
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

}