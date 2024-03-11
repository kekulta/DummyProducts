package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.CategoryDm
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.RerunType
import com.kekulta.dummyproducts.features.shared.exceptions.UnknownServerException
import logcat.logcat

class CategoryCacheRepository(
    private val categoryRepo: CategoryRepo,
) : AbstractCoroutineRepository() {
    private val cache = mutableMapOf<String, CategoryDm>()

    fun getCacheCategory(categoryName: String): CategoryDm? {
        return cache[categoryName]
    }

    fun updateCache(categoryName: String) {
        launchScope(scopeId = categoryName, rerunType = RerunType.KEEP) {
            updateCacheSync(categoryName)
        }
    }

    suspend fun updateCacheSync(categoryName: String): Result<CategoryDm> {
        val res = categoryRepo.getCategory(categoryName)
        val categoryRes = res.getOrNull()

        return if (res.isSuccess && categoryRes != null) {

            cache[categoryName] = categoryRes
            Result.success(categoryRes)
        } else {
            logcat { "Request failed: ${res.exceptionOrNull()}" }
            Result.failure(res.exceptionOrNull() ?: UnknownServerException())
        }
    }
}

