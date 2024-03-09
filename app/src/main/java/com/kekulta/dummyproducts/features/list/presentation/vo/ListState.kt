package com.kekulta.dummyproducts.features.list.presentation.vo

data class ListState(
    val content: List<ProductPreviewVo> = emptyList(), val pagingState: PagingVo = PagingVo(1, 1)
)