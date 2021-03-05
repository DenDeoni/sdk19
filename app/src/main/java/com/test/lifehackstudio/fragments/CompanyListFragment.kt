package com.test.lifehackstudio.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.lifehackstudio.*
import com.test.lifehackstudio.adapter.CommonAdapter
import com.test.lifehackstudio.databinding.FragmentCompanyListBinding
import com.test.lifehackstudio.viewbindingdelegate.viewBinding
import com.test.lifehackstudio.viewmodels.ListViewModel


class CompanyListFragment : Fragment(R.layout.fragment_company_list) {

    private val binding by viewBinding<FragmentCompanyListBinding>()
    private lateinit var companyDetailDialog: CompanyDetailDialog
    private val listViewModel: ListViewModel by viewModels()
    private var commonAdapter: CommonAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initList(emptyList())
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
        listViewModel.loadCompaniesList()
        listViewModel.companiesList.observe(viewLifecycleOwner) {
            commonAdapter?.items = it
        }
    }

    private fun showDialog(idItem: Long) {
        companyDetailDialog = CompanyDetailDialog()
        val args = Bundle()
        args.putParcelable(LIST_VIEW_MODEL, listViewModel)
        args.putLong(ID, idItem)
        companyDetailDialog.arguments = args
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