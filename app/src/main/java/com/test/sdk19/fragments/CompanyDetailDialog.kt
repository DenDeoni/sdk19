package com.test.sdk19.fragments

import android.app.Dialog
import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.support.v4.os.IResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.test.sdk19.*
import com.test.sdk19.viewbindingdelegate.DefaultViewBindingDialogFragment
import com.test.sdk19.viewbindingdelegate.dialogFragmentViewBinding
import com.test.sdk19.viewmodels.ListViewModel
import com.test.sdk19.databinding.DetailCompanyBinding


class CompanyDetailDialog : DefaultViewBindingDialogFragment() {
    private val binding by dialogFragmentViewBinding(
        R.id.rootContainer,
        DetailCompanyBinding::bind
    )

    companion object {
        fun newInstance(
            listViewModel: ListViewModel,
            idItem: Long
        ): CompanyDetailDialog {
            val dialog = CompanyDetailDialog()
            val bundle = Bundle()
            bundle.putParcelable(LIST_VIEW_MODEL, listViewModel)
            bundle.putLong(ID, idItem)
            dialog.arguments = bundle
            return dialog
        }
    }

    private lateinit var listViewModel: ListViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.detail_company)
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireArguments()
            .getParcelable<ListViewModel>(LIST_VIEW_MODEL)
            .also { if (it != null) listViewModel = it }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {// initializations
        val idCompany = Bundle().getLong(ID)
        println("idCompany: $idCompany")
        bindViewModel()
        super.onStart()
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        cardViewCorrectSdk19()
        closeWindow()
    }

    private fun bindViewModel() {
        val idCompany = requireArguments().getLong(ID)
        listViewModel.loadCompanyDetail(idCompany)

        listViewModel.companyDetail.observe(this) {
            val details = listViewModel.companyDetail.value
            if (details != null) {
                setDetailFields(details)
                hideEmptyFields(details)
            }
        }
    }

    private fun cardViewCorrectSdk19() {
        if (Build.VERSION.SDK_INT < 21) {
            binding.cardView.radius = 0F
        }
    }

    private fun setDetailFields(details: List<ContainerObjects.CompanyDetail>) {
        if (details.isNotEmpty()) {
            binding.textViewName.text = details[0].name
            Glide.with(binding.imageViewDetailLogo)
                .load(details[0].img)
                .into(binding.imageViewDetailLogo)
            binding.textViewDescription.text = details[0].description
            binding.textViewLat.text = details[0].lat.toString()
            binding.textViewLon.text = details[0].lon.toString()
            binding.textViewWeb.text = details[0].www
            binding.textViewPhone.text = details[0].phone
        }
    }

    private fun hideEmptyFields(details: List<ContainerObjects.CompanyDetail>) {
        if (details.isNotEmpty()) {
            if (details[0].lat == 0.0F) {
                binding.textViewLat.visibility = View.GONE
                binding.textLat.visibility = View.GONE
            }
            if (details[0].lon == 0.0F) {
                binding.textViewLon.visibility = View.GONE
                binding.textLon.visibility = View.GONE
            }
            if (details[0].www.isEmpty()) {
                binding.textViewWeb.visibility = View.GONE
                binding.textWeb.visibility = View.GONE
            }
            if (details[0].phone.isEmpty()) {
                binding.textViewPhone.visibility = View.GONE
                binding.textPhone.visibility = View.GONE
            }
        } else {
            binding.textLat.visibility = View.GONE
            binding.textLon.visibility = View.GONE
            binding.textWeb.visibility = View.GONE
            binding.textPhone.visibility = View.GONE
        }
    }

    private fun closeWindow() {
        binding.imageViewClose.setOnClickListener { dismiss() }
    }
}