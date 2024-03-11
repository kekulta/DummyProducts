package com.kekulta.dummyproducts.features.list.presentation.vo

import android.net.Uri

data class DetailsVo(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<Uri>,
    val recommendations: List<ProductPreviewVo>,
    val price: Double? = null,
    val oldPrice: Double? = null,
    val rating: Double? = null,
    val stock: Int? = null,
    val brand: String? = null,
    val category: String? = null,
)