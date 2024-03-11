package com.kekulta.dummyproducts.features.list.presentation.customviews

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.widget.LinearLayout
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.databinding.ListBottomBarLayoutBinding
import com.kekulta.dummyproducts.features.list.presentation.vo.PagingVo
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider
import com.kekulta.dummyproducts.features.shared.utils.getDrawable
import com.kekulta.dummyproducts.features.shared.utils.getMaterialColorStateList
import com.google.android.material.R as Rm


class ListBottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), UiEventProvider {
    private val binding: ListBottomBarLayoutBinding =
        ListBottomBarLayoutBinding.inflate(LayoutInflater.from(context), this)
    override var uiEventCallback: UiEventCallback? = null

    init {
        orientation = HORIZONTAL


        background = getDrawable(R.drawable.list_bottombar_background)
        backgroundTintList =
            getMaterialColorStateList(Rm.attr.colorSurfaceContainerHighest)
        gravity = Gravity.CENTER
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val insets = rootWindowInsets
            val bottomLeft =
                insets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)!!.radius.toFloat()
            val bottomRight =
                insets.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_RIGHT)!!.radius.toFloat()

            val shape = ShapeDrawable(
                RoundRectShape(
                    floatArrayOf(bottomRight, bottomRight, bottomLeft, bottomLeft, 0f, 0f, 0f, 0f),
                    null,
                    null
                )
            )
            background = shape
        }
    }

    fun bind(vo: PagingVo) {
        binding.prevPageButton.isEnabled = vo.currPage > 1
        binding.nextPageButton.isEnabled = vo.currPage < vo.pagesTotal

        binding.prevPageButton.setOnClickListener {
            dispatch(UiEvent.PageClicked(vo.currPage - 1))
        }
        binding.nextPageButton.setOnClickListener {
            dispatch(UiEvent.PageClicked(vo.currPage + 1))
        }

        binding.pageIndicator.text = "${vo.currPage}/${vo.pagesTotal}"
    }
}

