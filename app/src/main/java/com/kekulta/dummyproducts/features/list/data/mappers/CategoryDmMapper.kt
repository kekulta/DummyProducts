package com.kekulta.dummyproducts.features.list.data.mappers

import com.kekulta.dummyproducts.features.list.data.models.ProductsDto
import com.kekulta.dummyproducts.features.list.domain.models.CategoryDm
import com.kekulta.dummyproducts.features.list.domain.models.ProductDmMapper
import kotlin.math.ceil

class CategoryDmMapper(private val productDmMapper: ProductDmMapper) {
    fun map(productsDto: ProductsDto): CategoryDm {
        val pages = ceil(productsDto.total.toDouble() / productsDto.limit).toInt()
        val currPage =
            ceil((productsDto.skip + productsDto.products.size).toDouble() / productsDto.limit).toInt()
        return CategoryDm(
            content = productDmMapper.map(productsDto.products),
            currPage = currPage,
            pagesTotal = pages,
        )
    }
}