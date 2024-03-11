package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.ProductDataStore
import com.kekulta.dummyproducts.features.list.data.models.ProductDto
import com.kekulta.dummyproducts.features.list.data.api.DummyProductsApi

class ProductDataStoreImpl(private val api: DummyProductsApi) : ProductDataStore {
    override suspend fun getProduct(productId: Int): ProductDto {

        return api.getProducts(productId)
    }
}