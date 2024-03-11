package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.PageDm
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.RerunType
import com.kekulta.dummyproducts.features.shared.exceptions.UnknownServerException
import logcat.logcat

class PageCacheRepository(
    private val pageRepository: PageRepo,
) : AbstractCoroutineRepository() {
    private val pageCache = mutableMapOf<Int, PageDm>()

    fun getCachePage(page: Int): PageDm? {
        return pageCache[page]
    }

    fun updateCache(page: Int) {
        launchScope(scopeId = page, rerunType = RerunType.KEEP) {
            updateCacheSync(page)
        }
    }

    suspend fun updateCacheSync(page: Int): Result<PageDm> {
        val res = pageRepository.getPage(page)
        val pageRes = res.getOrNull()

        return if (res.isSuccess && pageRes != null) {

            pageCache[page] = pageRes
            Result.success(pageRes)
        } else {
            logcat { "Request failed: ${res.exceptionOrNull()}" }
            Result.failure(res.exceptionOrNull() ?: UnknownServerException())
        }
    }
}

