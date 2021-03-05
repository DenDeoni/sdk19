package com.test.lifehackstudio.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.test.lifehackstudio.ContainerObjects
import com.test.lifehackstudio.R

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