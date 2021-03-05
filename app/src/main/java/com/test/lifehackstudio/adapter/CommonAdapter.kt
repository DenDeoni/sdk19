package com.test.lifehackstudio.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.test.lifehackstudio.ContainerObjects

class CommonAdapter (
    onItemClick: (id: Long, position: Int, view: View) -> Unit
) : AsyncListDifferDelegationAdapter<ContainerObjects>(ObjectsDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(CompanyAdapterDelegate(onItemClick))
    }

    fun updateObjectsList(newContainerObjects: List<ContainerObjects>) {
        differ.submitList(newContainerObjects)
    }

    class ObjectsDiffUtilCallback : DiffUtil.ItemCallback<ContainerObjects>() {

        override fun areItemsTheSame(
            oldItem: ContainerObjects,
            newItem: ContainerObjects
        ): Boolean {
            return when {
                oldItem is ContainerObjects.Company && newItem is ContainerObjects.Company ->
                    oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: ContainerObjects,
            newItem: ContainerObjects
        ): Boolean {
            return oldItem == newItem
        }

    }

}