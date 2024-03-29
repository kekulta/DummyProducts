package com.kekulta.dummyproducts.features.list.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val discountPercentage: Double?,
    val rating: Double?,
    val stock: Int?,
    val brand: String?,
    val category: String?,
    val thumbnail: String?,
    val images: List<String>?,
)