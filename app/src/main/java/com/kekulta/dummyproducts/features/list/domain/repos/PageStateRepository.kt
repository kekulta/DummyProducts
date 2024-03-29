package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage
import com.kekulta.dummyproducts.features.list.domain.models.PageDm
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import logcat.logcat

class PageStateRepository(
    private val cache: PageCacheRepository,
    private val dispatcher: Dispatcher,
) : AbstractCoroutineRepository() {

    fun getPage(page: Int): Flow<PageDm> {
        return flow {
            cache.getCachePage(page).let { cached ->
                if (cached != null) {
                    emit(cached)
                }

                emitFromServer(page, cached != null)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<PageDm>.emitFromServer(page: Int, isCached: Boolean) {
        val text = if (isCached) {
            "Check your internet connection. Page loaded from cache and could be outdated."
        } else {
            "Check your internet connection."
        }

        val res = cache.updateCacheSync(page)
        val new = res.getOrNull()

        if (new == null) {
            logcat { "Server error: ${res.exceptionOrNull()}" }
            dispatcher.dispatch(
                UiEvent.Message(
                    AlertMessage(
                        "Connection error!",
                        text,
                    )
                )
            )
            return
        }

        emit(new)
    }
}