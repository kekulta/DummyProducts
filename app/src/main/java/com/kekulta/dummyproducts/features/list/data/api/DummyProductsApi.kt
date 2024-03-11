package com.kekulta.dummyproducts.features.list.data.api

import com.kekulta.dummyproducts.features.list.data.models.ProductDto
import com.kekulta.dummyproducts.features.list.data.models.ProductsDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface DummyProductsApi {

    @GET("products")
    suspend fun getProducts(@Query("skip") skip: Int, @Query("limit") limit: Int): ProductsDto

    @GET("products/{productId}")
    suspend fun getProducts(@Path("productId") productId: Int): ProductDto

    @GET("products/category/{categoryName}")
    suspend fun getCategory(
        @Path("categoryName") categoryName: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductsDto
}