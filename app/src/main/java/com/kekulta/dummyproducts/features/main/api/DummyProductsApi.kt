package com.kekulta.dummyproducts.features.main.api

import com.kekulta.dummyproducts.features.list.data.models.ProductsDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface DummyProductsApi {

    @GET("products")
    suspend fun getProducts(@Query("skip") skip: Int, @Query("limit") limit: Int): ProductsDto
}