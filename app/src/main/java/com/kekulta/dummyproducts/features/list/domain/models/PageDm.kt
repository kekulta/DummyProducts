package com.kekulta.dummyproducts.features.list.domain.models

import com.kekulta.dummyproducts.features.list.domain.models.ProductDm

data class PageDm(val content: List<ProductDm>, val currPage: Int, val pagesTotal: Int)