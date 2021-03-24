package com.test.sdk19.adapter

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.test.sdk19.ContainerObjects
import com.test.sdk19.extensions.inflate
import com.test.sdk19.R

class CompanyAdapterDelegate(
    private val onItemClick: (id: Long, position: Int, view: View) -> Unit
) : AbsListItemAdapterDelegate<ContainerObjects.Company,
        ContainerObjects,
        CompanyAdapterDelegate.CompanyHolder>() {

    class CompanyHolder(
        view: View,
        onItemClick: (id: Long, position: Int, view: View) -> Unit
    ) : BasicHolder(view, onItemClick, view) {
        fun bind(company: ContainerObjects.Company) {
            bindMainInfo(company.id, company.name, company.img)
        }
    }

    override fun isForViewType(
        item: ContainerObjects,
        items: MutableList<ContainerObjects>,
        position: Int
    ): Boolean {
        return item is ContainerObjects.Company
    }

    override fun onCreateViewHolder(parent: ViewGroup): CompanyHolder {
        return CompanyHolder(
            parent.inflate(R.layout.item_company),
            onItemClick
        )
    }

    override fun onBindViewHolder(
        item: ContainerObjects.Company,
        holder: CompanyHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}