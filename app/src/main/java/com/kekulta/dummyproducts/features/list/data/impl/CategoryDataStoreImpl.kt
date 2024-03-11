package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.CategoryDataStore
import com.kekulta.dummyproducts.features.list.data.api.DummyProductsApi
import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

class CategoryDataStoreImpl(private val api: DummyProductsApi) : CategoryDataStore {
    override suspend fun getCategory(categoryName: String, skip: Int, limit: Int): ProductsDto {
        return api.getCategory(categoryName, skip, limit)
    }
}