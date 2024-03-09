package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.presentation.vo.ListState
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.RerunType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import logcat.logcat

class PageStateRepository(
    private val cache: PageCacheRepository,
) : AbstractCoroutineRepository() {
    private val state = MutableStateFlow(ListState())

    fun observeState(): StateFlow<ListState> = state

    fun setPage(page: Int) {
        cache.getCachePage(page).let { cached ->
            if (cached != null) {
                state.update { cached }
                updateFromServer(page)
            } else {
                state.getAndUpdate { old ->
                    old.copy(
                        content = emptyList(),
                        pagingState = old.pagingState.copy(currPage = page)
                    )
                }
                updateFromServer(page)
            }

            cache.updateCache(page + 1)
            cache.updateCache(page + 2)
        }
    }


    private fun updateFromServer(page: Int) {
        launchScope(page, RerunType.KEEP) {
            val res = cache.updateCacheSync(page)
            val new = res.getOrNull()

            if (new == null) {
                logcat { "Server error: ${res.exceptionOrNull()}" }
                return@launchScope
            }

            state.getAndUpdate { old ->
                if (old.pagingState.currPage == new.pagingState.currPage) {
                    new
                } else {
                    old
                }
            }
        }
    }
}