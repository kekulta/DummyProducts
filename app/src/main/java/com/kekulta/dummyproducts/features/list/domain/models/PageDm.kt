package com.kekulta.dummyproducts.features.list.domain.models

data class PageDm(val content: List<ProductDm>, val currPage: Int, val pagesTotal: Int)