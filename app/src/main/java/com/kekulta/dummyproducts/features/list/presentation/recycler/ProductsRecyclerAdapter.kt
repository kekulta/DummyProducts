package com.kekulta.dummyproducts.features.list.presentation.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kekulta.dummyproducts.features.list.presentation.customviews.ProductPreviewView
import com.kekulta.dummyproducts.features.list.presentation.vo.ProductPreviewVo
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider

class ProductsRecyclerAdapter :
    ListAdapter<ProductPreviewVo, ProductsRecyclerAdapter.ProductViewHolder>(DIFF_CALLBACK),
    UiEventProvider {
    override var uiEventCallback: UiEventCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductPreviewView(parent.context))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.uiEventCallback = ::dispatch
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val view: ProductPreviewView) : RecyclerView.ViewHolder(view),
        UiEventProvider {
        override var uiEventCallback: UiEventCallback? = null

        fun bind(vo: ProductPreviewVo) {
            view.bind(vo)
            view.uiEventCallback = ::dispatch
            dispatch(UiEvent.PreviewLoaded(vo.id))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductPreviewVo>() {
            override fun areItemsTheSame(
                oldItem: ProductPreviewVo,
                newItem: ProductPreviewVo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductPreviewVo,
                newItem: ProductPreviewVo
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}