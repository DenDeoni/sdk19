package com.test.lifehackstudio.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.test.lifehackstudio.ContainerObjects
import com.test.lifehackstudio.ID
import com.test.lifehackstudio.LIST_VIEW_MODEL
import com.test.lifehackstudio.R
import com.test.lifehackstudio.databinding.DetailCompanyBinding
import com.test.lifehackstudio.viewbindingdelegate.DefaultViewBindingDialogFragment
import com.test.lifehackstudio.viewbindingdelegate.dialogFragmentViewBinding
import com.test.lifehackstudio.viewmodels.ListViewModel


class CompanyDetailDialog : DefaultViewBindingDialogFragment() {
    private val binding by dialogFragmentViewBinding(
        R.id.rootContainer,
        DetailCompanyBinding::bind
    )

    private lateinit var listViewModel: ListViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            //.setTitle(getString(R.string.company_detail))
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

    override fun onStart() { // initializations
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