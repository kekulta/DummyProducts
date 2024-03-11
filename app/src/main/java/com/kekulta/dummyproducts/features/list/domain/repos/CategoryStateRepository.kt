package com.kekulta.dummyproducts.features.list.domain.repos

import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage
import com.kekulta.dummyproducts.features.list.domain.models.CategoryDm
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import logcat.logcat

class CategoryStateRepository(
    private val cache: CategoryCacheRepository,
    private val dispatcher: Dispatcher,
) : AbstractCoroutineRepository() {

    fun getCategory(categoryName: String): Flow<CategoryDm> {
        return flow {
            cache.getCacheCategory(categoryName).let { cached ->
                if (cached != null) {
                    emit(cached)
                } else {
                    emit(
                        CategoryDm(
                            emptyList(),
                            1, 1
                        )
                    )
                }

                emitFromServer(categoryName, cached != null)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<CategoryDm>.emitFromServer(
        categoryName: String,
        isCached: Boolean = false
    ) {
        val res = cache.updateCacheSync(categoryName)
        val new = res.getOrNull()

        val text = if (isCached) {
            "Check your internet connection. Page loaded from cache and could be outdated."
        } else {
            "Check your internet connection."
        }

        if (new == null) {
            logcat { "Server error: ${res.exceptionOrNull()}" }
            dispatcher.dispatch(UiEvent.Message(AlertMessage("Connection error!", text)))
            return
        }

        emit(new)
    }
}