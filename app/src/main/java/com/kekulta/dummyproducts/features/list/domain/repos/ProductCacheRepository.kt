package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.ProductDm
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.RerunType
import com.kekulta.dummyproducts.features.shared.exceptions.UnknownServerException
import logcat.logcat

class ProductCacheRepository(
    private val productRepo: ProductRepo,
    private val categoryCacheRepository: CategoryCacheRepository,
) : AbstractCoroutineRepository() {
    private val detailsCache = mutableMapOf<Int, ProductDm>()

    fun getDetailsCache(productId: Int): ProductDm? {
        return detailsCache[productId]
    }

    fun loadCache(productId: Int) {
        if (detailsCache[productId] == null) {
            updateCache(productId)
        }
    }

    fun updateCache(productId: Int) {
        launchScope(scopeId = productId, rerunType = RerunType.KEEP) {
            updateCacheSync(productId)
        }
    }

    suspend fun updateCacheSync(productId: Int): Result<ProductDm> {
        val res = productRepo.getProduct(productId)
        val productRes = res.getOrNull()

        return if (res.isSuccess && productRes != null) {

            detailsCache[productId] = productRes
            productRes.category?.let { categoryName ->
                categoryCacheRepository.updateCache(categoryName)
            }
            Result.success(productRes)
        } else {
            logcat { "Request failed: ${res.exceptionOrNull()}" }
            Result.failure(res.exceptionOrNull() ?: UnknownServerException())
        }
    }
}

