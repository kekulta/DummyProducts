package com.kekulta.dummyproducts.features.list.presentation.customviews


import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.ProductCardLayoutBinding
import com.kekulta.dummyproducts.features.list.presentation.recycler.CarouselRecyclerAdapter
import com.kekulta.dummyproducts.features.list.presentation.vo.DetailsVo
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider
import com.kekulta.dummyproducts.features.shared.utils.applyOrGone
import com.kekulta.dummyproducts.features.shared.utils.getDrawable
import com.kekulta.dummyproducts.features.shared.utils.getMaterialColorStateList
import com.google.android.material.R as Rm


class ProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Rm.attr.materialCardViewElevatedStyle,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), UiEventProvider {
    private val binding: ProductCardLayoutBinding =
        ProductCardLayoutBinding.inflate(LayoutInflater.from(context), this)
    override var uiEventCallback: UiEventCallback? = null
    private val recAdapter = CarouselRecyclerAdapter()

    init {
        binding.productCarousel.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recAdapter
            PagerSnapHelper().attachToRecyclerView(this)
        }
        clipToOutline = true
        background = getDrawable(R.drawable.card_background)
        backgroundTintList = getMaterialColorStateList(Rm.attr.colorSurfaceContainerLow)
    }

    fun bind(vo: DetailsVo) {
        recAdapter.submitList(vo.images)

        binding.productTitle.text = vo.title
        binding.productDescription.text = vo.description

        binding.productNewPrice.applyOrGone(vo.price) {
            text = it.toString()
        }

        binding.productOldPrice.applyOrGone(vo.oldPrice) {
            text = it.toString()
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.productRating.applyOrGone(vo.rating) {
            rating = it.toFloat()
        }
    }
}

