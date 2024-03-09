package com.kekulta.dummyproducts.features.main.api

import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

interface NetworkDataStore {
    suspend fun getProducts(skip: Int, limit: Int): ProductsDto
}