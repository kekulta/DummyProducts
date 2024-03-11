package com.kekulta.dummyproducts.features.list.domain.viewmodels

import com.kekulta.dummyproducts.features.list.domain.formatters.DetailsVoFormatter
import com.kekulta.dummyproducts.features.list.domain.repos.CategoryStateRepository
import com.kekulta.dummyproducts.features.list.domain.repos.ProductStateRepository
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.list.presentation.vo.DetailsVo
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineViewModel
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import logcat.logcat

class DetailsViewModel(
    private val productStateRepository: ProductStateRepository,
    private val categoryStateRepository: CategoryStateRepository,
    private val detailsVoFormatter: DetailsVoFormatter,
    private val dispatcher: Dispatcher,
) : AbstractCoroutineViewModel(), UiEventDispatcher {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getDetails(productId: Int): Flow<DetailsVo> {
        val details = productStateRepository.getDetails(productId).flatMapLatest { productDm ->

            logcat { "Details from repo: $productDm" }
            if (productDm.category == null) {
                logcat { "Format details: $productDm" }
                flow<DetailsVo> { emit(detailsVoFormatter.format(productDm, emptyList())) }
            } else {
                categoryStateRepository.getCategory(productDm.category).map { categoryDm ->
                    val recs = if (categoryDm.content.size % 2 == 1) {
                        categoryDm.content.dropLast(1)
                    } else {
                        categoryDm.content
                    }
                    detailsVoFormatter.format(productDm, recs)
                }
            }
        }.flowOn(Dispatchers.IO)

        return details
    }

    override fun dispatch(event: UiEvent) {
        when (event) {
            else -> {
                logcat { "Unhandled event: $event" }
                dispatcher.dispatch(event)
            }
        }
    }
}

