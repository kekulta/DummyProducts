package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.main.api.DummyProductsApi
import com.kekulta.dummyproducts.features.main.api.NetworkDataStore
import com.kekulta.dummyproducts.features.list.data.models.ProductsDto

class NetworkDataStoreImpl(private val api: DummyProductsApi) : NetworkDataStore {
    override suspend fun getProducts(skip: Int, limit: Int): ProductsDto {
        return api.getProducts(skip, limit)
    }
}