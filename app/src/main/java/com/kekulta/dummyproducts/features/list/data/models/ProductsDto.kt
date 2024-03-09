package com.kekulta.dummyproducts.features.list.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductsDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)