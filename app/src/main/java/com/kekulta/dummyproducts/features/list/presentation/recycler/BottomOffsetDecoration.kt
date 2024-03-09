package com.kekulta.dummyproducts.features.list.presentation.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


internal class BottomOffsetDecoration(private val mBottomOffset: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val dataSize = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (dataSize == 0) {
            outRect[0, 0, 0] = 0
            return
        }

        if (dataSize % 2 == 1 && position == dataSize - 1) {
            outRect[0, 0, 0] = mBottomOffset
        }

        if (dataSize % 2 == 0 && (position == dataSize - 1 || position == dataSize - 2)) {
            outRect[0, 0, 0] = mBottomOffset
        }
    }
}