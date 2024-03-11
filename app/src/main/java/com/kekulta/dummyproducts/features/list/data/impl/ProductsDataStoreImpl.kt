package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.DummyProductsApi
import com.kekulta.dummyproducts.features.list.data.api.ProductsDataStore
import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

class ProductsDataStoreImpl(private val api: DummyProductsApi) : ProductsDataStore {
    override suspend fun getProducts(skip: Int, limit: Int): ProductsDto {
        return api.getProducts(skip, limit)
    }
}