package com.kekulta.dummyproducts.features.list.presentation.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.NO_POSITION


class DetailsSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val isLeadCard = position == 0
        if (position == NO_POSITION) return

        if (isLeadCard) {
            outRect.bottom = spacing
            return
        }

        val column = (position + 1) % spanCount // item column

        outRect.left =
            spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
        outRect.right =
            (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

        // item bottom
        outRect.bottom = spacing
    }
}
