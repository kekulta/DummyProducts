package com.kekulta.dummyproducts.features.list.presentation.recycler

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.kekulta.dummyproducts.R
import com.kekulta.dummyproducts.features.list.presentation.customviews.ProductCardView
import com.kekulta.dummyproducts.features.list.presentation.customviews.ProductPreviewView
import com.kekulta.dummyproducts.features.list.presentation.vo.DetailsVo
import com.kekulta.dummyproducts.features.list.presentation.vo.ProductPreviewVo
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider
import com.kekulta.dummyproducts.features.shared.utils.dip
import com.kekulta.dummyproducts.features.shared.utils.getDrawable
import com.kekulta.dummyproducts.features.shared.utils.getMaterialColor
import logcat.LogPriority
import logcat.logcat
import com.google.android.material.R as Rm

class DetailsRecyclerAdapter :
    ListAdapter<DetailsRecyclerAdapter.ProductDetailsSection, DetailsRecyclerAdapter.SectionViewHolder>(
        DIFF_CALLBACK
    ),
    UiEventProvider {
    override var uiEventCallback: UiEventCallback? = null

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val item = getItem(position)
            return if (item is ProductDetailsSection.Preview) {
                1
            } else {
                2
            }
        }
    }

    fun submitDetails(vo: DetailsVo) {
        submitList(buildList<ProductDetailsSection> {
            add(ProductDetailsSection.Card(vo))
            if (vo.recommendations.isNotEmpty() && vo.category != null) {
                add(ProductDetailsSection.Text("More from the category ${vo.category}:"))
            }
            vo.recommendations.forEach { rec ->
                add(ProductDetailsSection.Preview(rec))
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        return when (viewType) {
            ProductDetailsSection.PREVIEW -> SectionViewHolder.Preview(ProductPreviewView(parent.context))
            ProductDetailsSection.CARD -> SectionViewHolder.Card(ProductCardView(parent.context))
            ProductDetailsSection.TEXT -> SectionViewHolder.Text(TextView(parent.context))
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }.also { vh -> vh.uiEventCallback = ::dispatch }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductDetailsSection.Preview -> ProductDetailsSection.PREVIEW
            is ProductDetailsSection.Card -> ProductDetailsSection.CARD
            is ProductDetailsSection.Text -> ProductDetailsSection.TEXT
        }
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is SectionViewHolder.Card && item is ProductDetailsSection.Card) {
            holder.bind(item.card)
        } else if (holder is SectionViewHolder.Preview && item is ProductDetailsSection.Preview) {
            holder.bind(item.preview, position, itemCount)
        } else if (holder is SectionViewHolder.Text && item is ProductDetailsSection.Text) {
            holder.bind(item.text)
        } else {
            logcat(LogPriority.ERROR) { "Unhandled bind: $holder, $item" }
        }
    }

    sealed class ProductDetailsSection() {
        data class Preview(val preview: ProductPreviewVo) : ProductDetailsSection()
        data class Card(val card: DetailsVo) : ProductDetailsSection()
        data class Text(val text: String) : ProductDetailsSection()

        companion object {
            const val CARD = 0
            const val PREVIEW = 1
            const val TEXT = 2
        }
    }

    sealed class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view),
        UiEventProvider {
        override var uiEventCallback: UiEventCallback? = null

        class Text(private val view: TextView) : SectionViewHolder(view) {
            init {
                view.apply {
                    val padding = 16
                    setTextAppearance(Rm.style.TextAppearance_Material3_TitleLarge)
                    setPadding(dip(padding), dip(padding), dip(padding), dip(padding))
                    layoutParams = FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, dip(8))
                    }
                    background = getDrawable(R.drawable.card_background)
                }
            }

            fun bind(vo: String) {
                view.text = vo
            }
        }

        class Preview(
            private val view: ProductPreviewView,
            private val wrappedView: FrameLayout = FrameLayout(view.context).apply {
                addView(view)
            }
        ) : SectionViewHolder(wrappedView) {
            fun bind(vo: ProductPreviewVo, position: Int, count: Int) {
                setupMargins(position, count)

                view.bind(vo)
                view.uiEventCallback = ::dispatch
                dispatch(UiEvent.PreviewLoaded(vo.id))
            }

            private fun setupMargins(position: Int, count: Int) {
                if (position == NO_POSITION) return

                val nonPreviewItems = 2

                wrappedView.apply {
                    val radius = resources.getDimension(R.dimen.product_card_corner_radius)
                    val margin = resources.getDimension(R.dimen.carousel_margin).toInt() / 2

                    var topLeftRadius = 0f
                    var topRightRadius = 0f
                    var bottomRightRadius = 0f
                    var bottomLeftRadius = 0f

                    var topMargin = 0
                    var endMargin = 0
                    var bottomMargin = 0
                    var startMargin = 0

                    when (position) {
                        nonPreviewItems -> {
                            topLeftRadius = radius
                            topMargin = margin
                        }

                        nonPreviewItems + 1 -> {
                            topRightRadius = radius
                            topMargin = margin
                        }

                        count - 1 -> {
                            bottomRightRadius = radius
                            bottomMargin = margin
                        }

                        count - 2 -> {
                            bottomLeftRadius = radius
                            bottomMargin = margin
                        }
                    }

                    if (position % 2 != nonPreviewItems % 2) {
                        endMargin = margin
                    } else {
                        startMargin = margin
                    }

                    view.layoutParams = FrameLayout.LayoutParams(view.layoutParams).apply {
                        setMargins(
                            margin + startMargin,
                            margin + topMargin,
                            margin + endMargin,
                            margin + bottomMargin,
                        )
                    }


                    val shape = ShapeDrawable(
                        RoundRectShape(
                            floatArrayOf(
                                topLeftRadius,
                                topLeftRadius,
                                topRightRadius,
                                topRightRadius,
                                bottomRightRadius,
                                bottomRightRadius,
                                bottomLeftRadius,
                                bottomLeftRadius
                            ),
                            null,
                            null
                        )
                    )
                    shape.paint.color = getMaterialColor(Rm.attr.colorSurfaceContainerLow)

                    background = shape
                }
            }
        }

        class Card(
            private val view: ProductCardView,
            private val wrappedView: FrameLayout = FrameLayout(view.context).apply {
                addView(view)
            }
        ) : SectionViewHolder(wrappedView) {

            init {
                wrappedView.apply {
                    layoutParams = FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, dip(16))
                    }
                }
            }

            fun bind(vo: DetailsVo) {
                view.bind(vo)
                view.uiEventCallback = ::dispatch
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDetailsSection>() {
            override fun areItemsTheSame(
                oldItem: ProductDetailsSection,
                newItem: ProductDetailsSection
            ): Boolean {
                if (oldItem is ProductDetailsSection.Text && newItem is ProductDetailsSection.Text) {
                    return oldItem.text == newItem.text
                }

                if (oldItem is ProductDetailsSection.Card && newItem is ProductDetailsSection.Card) {
                    return oldItem.card.id == newItem.card.id
                }

                if (oldItem is ProductDetailsSection.Preview && newItem is ProductDetailsSection.Preview) {
                    return oldItem.preview.id == newItem.preview.id
                }

                return false
            }

            override fun areContentsTheSame(
                oldItem: ProductDetailsSection,
                newItem: ProductDetailsSection
            ): Boolean {
                if (oldItem is ProductDetailsSection.Text && newItem is ProductDetailsSection.Text) {
                    return oldItem.text == newItem.text
                }

                if (oldItem is ProductDetailsSection.Card && newItem is ProductDetailsSection.Card) {
                    return oldItem.card == newItem.card
                }

                if (oldItem is ProductDetailsSection.Preview && newItem is ProductDetailsSection.Preview) {
                    return oldItem.preview == newItem.preview
                }

                return false
            }

        }
    }
}