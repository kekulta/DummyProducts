package com.kekulta.dummyproducts.features.list.data.api

import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

interface CategoryDataStore {
    suspend fun getCategory(categoryName: String, skip: Int, limit: Int): ProductsDto
}