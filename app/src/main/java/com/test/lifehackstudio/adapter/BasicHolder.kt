package com.test.lifehackstudio.adapter

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.lifehackstudio.R
import kotlinx.android.extensions.LayoutContainer

abstract class BasicHolder(
    view: View,
    onItemClick: (id: Long, position: Int, view: View) -> Unit,
    override val containerView: View?,
    private var currentId: Long? = null
) : RecyclerView.ViewHolder(view), LayoutContainer {

    private val imageView: ImageView = view.findViewById(R.id.imageViewItemLogo)
    private val textViewName: TextView = view.findViewById(R.id.textViewName)
    private val cardView: CardView = view.findViewById(R.id.card_view)

    init {
        view.setOnClickListener {
            currentId?.let { itemId -> onItemClick(itemId, adapterPosition, it) }
        }
    }

    protected fun bindMainInfo(
        id: Long,
        name: String,
        imageLink: String
    ) {
        currentId = id
        textViewName.text = name
        Glide.with(itemView)
            .load(imageLink)
            .into(imageView)
        cardViewCorrectSdk19()
    }
    private fun cardViewCorrectSdk19() {
        if (Build.VERSION.SDK_INT < 21) {
            cardView.radius = 0F
        }
    }
}