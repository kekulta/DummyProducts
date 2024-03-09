package com.kekulta.dummyproducts.features.list.domain.formatters

import com.kekulta.dummyproducts.features.list.domain.models.PageDm
import com.kekulta.dummyproducts.features.list.presentation.vo.ListState
import com.kekulta.dummyproducts.features.list.presentation.vo.PagingVo

class ListStateFormatter(
    private val productFormatter: ProductPreviewVoFormatter,
) {
    fun format(pageDm: PageDm): ListState {
        return ListState(
            content = productFormatter.format(pageDm.content),
            pagingState = PagingVo(
                currPage = pageDm.currPage,
                pagesTotal = pageDm.pagesTotal,
            ),
        )
    }
}