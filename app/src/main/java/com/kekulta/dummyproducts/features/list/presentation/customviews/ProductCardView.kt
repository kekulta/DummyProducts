package com.kekulta.dummyproducts.features.list.presentation.customviews


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.ProductCardLayoutBinding
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider
import com.kekulta.dummyproducts.features.list.presentation.vo.ProductPreviewVo
import com.google.android.material.R as Rm


class ProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Rm.attr.materialCardViewElevatedStyle,
    defStyleRes: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), UiEventProvider {
    private val binding: ProductCardLayoutBinding =
        ProductCardLayoutBinding.inflate(LayoutInflater.from(context), this)
    override var uiEventCallback: UiEventCallback? = null

    init {
        val radius = resources.getDimension(R.dimen.product_card_corner_radius)
        shapeAppearanceModel = shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCornerSize(radius)
            .setBottomLeftCornerSize(radius)
            .build();
    }

    fun bind(vo: ProductPreviewVo) {
        binding.apply {
            Glide.with(this@ProductCardView)
                .load(vo.thumbnail)
                .into(productThumbnail)


            productTitle.text = vo.title
            productDescription.text = vo.description

            setOnClickListener {

                dispatch(UiEvent.OpenDetails(vo.id))
            }
        }
    }
}

