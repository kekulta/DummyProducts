package com.kekulta.dummyproducts.features.list.domain.models

import android.net.Uri

class ProductDm(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Uri,
    val price: Double?,
    val discountPercentage: Double?,
    val rating: Double?,
    val stock: Int?,
    val brand: String?,
    val category: String?,
    val images: List<String>?,
)