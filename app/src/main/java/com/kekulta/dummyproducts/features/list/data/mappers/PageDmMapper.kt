package com.kekulta.dummyproducts.features.list.data.mappers

import com.kekulta.dummyproducts.features.list.domain.models.PageDm
import com.kekulta.dummyproducts.features.list.data.models.ProductsDto
import kotlin.math.ceil

class PageDmMapper(private val productDmMapper: ProductDmMapper) {
    fun map(productsDto: ProductsDto): PageDm {
        val pages = ceil(productsDto.total.toDouble() / productsDto.limit).toInt()
        val currPage =
            ceil((productsDto.skip + productsDto.products.size).toDouble() / productsDto.limit).toInt()
        return PageDm(
            content = productDmMapper.map(productsDto.products),
            currPage = currPage,
            pagesTotal = pages,
        )
    }
}