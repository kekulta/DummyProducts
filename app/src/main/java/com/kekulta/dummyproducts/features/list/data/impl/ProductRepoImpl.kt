package com.kekulta.dummyproducts.features.list.data.impl

import com.kekulta.dummyproducts.features.list.data.api.ProductDataStore
import com.kekulta.dummyproducts.features.list.domain.models.ProductDm
import com.kekulta.dummyproducts.features.list.domain.models.ProductDmMapper
import com.kekulta.dummyproducts.features.list.domain.repos.ProductRepo
import com.kekulta.dummyproducts.features.shared.exceptions.IncorrectProductException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepoImpl(
    private val datastore: ProductDataStore,
    private val productDmMapper: ProductDmMapper,
) : ProductRepo {
    override suspend fun getProduct(productId: Int): Result<ProductDm> {

        return try {
            withContext(Dispatchers.IO) {
                val product =
                    productDmMapper.map(listOf(datastore.getProduct(productId))).firstOrNull()


                if (product == null) {
                    Result.failure(IncorrectProductException("Incorrect product $productId"))
                } else {
                    Result.success(product)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

