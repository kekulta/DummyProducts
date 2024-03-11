package com.kekulta.dummyproducts.features.list.data.api

import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

interface ProductsDataStore {
    suspend fun getProducts(skip: Int, limit: Int): ProductsDto
}