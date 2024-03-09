package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.exceptions.UnknownServerException
import com.kekulta.dummyproducts.features.list.domain.formatters.ListStateFormatter
import com.kekulta.dummyproducts.features.list.presentation.vo.ListState
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.RerunType
import logcat.logcat

class PageCacheRepository(
    private val pageRepository: PageRepo,
    private val listStateFormatter: ListStateFormatter,
) : AbstractCoroutineRepository() {
    private val pageCache = mutableMapOf<Int, ListState>()

    fun getCachePage(page: Int): ListState? {
        return pageCache[page]
    }

    fun updateCache(page: Int) {
        launchScope(scopeId = page, rerunType = RerunType.KEEP) {
            updateCacheSync(page)
        }
    }

    suspend fun updateCacheSync(page: Int): Result<ListState> {
        val res = pageRepository.getPage(page)
        val pageRes = res.getOrNull()

        return if (res.isSuccess && pageRes != null) {
            val new = listStateFormatter.format(pageRes)

            pageCache[page] = new
            Result.success(new)
        } else {
            logcat { "Request failed: ${res.exceptionOrNull()}" }
            Result.failure(res.exceptionOrNull() ?: UnknownServerException())
        }
    }
}

