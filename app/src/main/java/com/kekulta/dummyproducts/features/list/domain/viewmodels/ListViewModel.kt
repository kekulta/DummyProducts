package com.kekulta.dummyproducts.features.list.domain.viewmodels

import com.kekulta.dummyproducts.features.list.domain.formatters.ListStateFormatter
import com.kekulta.dummyproducts.features.list.domain.repos.PageCacheRepository
import com.kekulta.dummyproducts.features.list.domain.repos.PageStateRepository
import com.kekulta.dummyproducts.features.list.domain.repos.ProductCacheRepository
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.list.presentation.vo.ListState
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineViewModel
import com.kekulta.dummyproducts.features.shared.RerunType
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import logcat.LogPriority
import logcat.logcat

class ListViewModel(
    private val pageStateRepository: PageStateRepository,
    private val productCacheRepository: ProductCacheRepository,
    private val pageCacheRepository: PageCacheRepository,
    private val listStateFormatter: ListStateFormatter,
    private val dispatcher: Dispatcher,
) : AbstractCoroutineViewModel(), UiEventDispatcher {
    private var loading = 0
    private val _state = MutableStateFlow(ListState())
    fun observeState(): StateFlow<ListState> = _state

    init {
        setPage(1)
    }

    fun prevPage(): Boolean {
        val page = _state.value.pagingState.currPage - 1

        if (page < 1) return false
        setPage(page)
        return true
    }

    private fun setPage(page: Int) {
        if (page < 1 || page > _state.value.pagingState.pagesTotal) {
            logcat(LogPriority.ERROR) {
                "Incorrect page: $page"
            }
        }
        if (loading == page) return
        loading = page

        launchScope(page + 1, RerunType.KEEP) {
            pageCacheRepository.updateCache(page + 1)
        }

        launchScope(page + 2, RerunType.KEEP) {
            pageCacheRepository.updateCache(page + 2)
        }

        launchScope {
            pageStateRepository.getPage(page).collect { state ->
                _state.update { listStateFormatter.format(state) }
            }
        }
    }

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.PageClicked -> setPage(event.num)
            is UiEvent.PreviewLoaded -> productCacheRepository.loadCache(event.id)
            else -> {
                logcat { "Unhandled event: $event" }
                dispatcher.dispatch(event)
            }
        }
    }
}

