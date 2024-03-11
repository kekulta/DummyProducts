package com.kekulta.dummyproducts.features.list.presentation.recycler

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout.LayoutParams
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider
import com.kekulta.dummyproducts.features.shared.utils.getDrawable
import com.kekulta.dummyproducts.features.shared.utils.getMaterialColorStateList
import com.google.android.material.R as Rm

class CarouselRecyclerAdapter() : ListAdapter<Uri, CarouselRecyclerAdapter.ImageViewHolder>(
    DIFF_CALLBACK
), UiEventProvider {
    override var uiEventCallback: UiEventCallback? = null

    class ImageViewHolder(private val view: ImageView) : RecyclerView.ViewHolder(view) {
        init {
            view.apply {
                background = getDrawable(R.drawable.carousel_background)
                backgroundTintList = getMaterialColorStateList(Rm.attr.colorSurfaceContainer)
                clipToOutline = true
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(resources.getDimension(R.dimen.carousel_margin).toInt())
                }
            }
        }

        fun bind(uri: Uri) {
            Glide.with(view).load(uri).placeholder(R.drawable.carousel_background).into(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageView(parent.context))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ImageViewHolder) {
        super.onViewAttachedToWindow(holder)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }
        }
    }
}