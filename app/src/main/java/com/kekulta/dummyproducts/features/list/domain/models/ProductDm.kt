package com.kekulta.dummyproducts.features.list.domain.models

import android.net.Uri

data class ProductDm(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Uri,
    val price: Double? = null,
    val discountPercentage: Double? = null,
    val rating: Double? = null,
    val stock: Int? = null,
    val brand: String? = null,
    val category: String? = null,
    val images: List<String>? = null,
)