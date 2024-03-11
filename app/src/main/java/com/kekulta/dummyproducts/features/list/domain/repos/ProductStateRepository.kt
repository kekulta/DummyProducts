package com.kekulta.dummyproducts.features.list.domain.repos

import android.net.Uri
import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage
import com.kekulta.dummyproducts.features.list.domain.models.ProductDm
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineRepository
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import logcat.logcat

class ProductStateRepository(
    private val cache: ProductCacheRepository,
    private val dispatcher: Dispatcher,
) : AbstractCoroutineRepository() {
    fun getDetails(productId: Int): Flow<ProductDm> {
        return flow {
            cache.getDetailsCache(productId).let { cached ->
                if (cached != null) {
                    emit(cached)
                } else {
                    emit(
                        ProductDm(
                            id = 0,
                            title = "Loading...",
                            description = "Loading...",
                            thumbnail = Uri.parse("android.resource://com.kekulta.dummyproducts/R.drawable.thumbnail_background"),
                        )
                    )
                }

                emitFromServer(productId, cached != null)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<ProductDm>.emitFromServer(
        page: Int,
        isCached: Boolean = false
    ) {
        val res = cache.updateCacheSync(page)
        val new = res.getOrNull()

        if (new == null) {
            logcat { "Server error: ${res.exceptionOrNull()}" }
            val text = if (isCached) {
                "Check your internet connection. Page loaded from cache and could be outdated."
            } else {
                "Check your internet connection."
            }

            dispatcher.dispatch(
                UiEvent.Message(
                    AlertMessage(
                        "Connection error!",
                        text
                    )
                )
            )
            return
        }

        emit(new)
    }
}