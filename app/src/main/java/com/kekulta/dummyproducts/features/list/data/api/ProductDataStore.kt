package com.kekulta.dummyproducts.features.list.data.api

import com.kekulta.dummyproducts.features.list.data.models.ProductDto

interface ProductDataStore {
    suspend fun getProduct(productId: Int): ProductDto
}