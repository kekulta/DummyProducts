package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.ProductDm

interface ProductRepo {
    suspend fun getProduct(productId: Int): Result<ProductDm>
}