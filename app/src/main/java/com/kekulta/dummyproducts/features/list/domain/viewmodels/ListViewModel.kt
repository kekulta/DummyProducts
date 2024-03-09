package com.kekulta.dummyproducts.features.list.domain.viewmodels

import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventDispatcher
import com.kekulta.dummyproducts.features.list.domain.repos.PageStateRepository
import com.kekulta.dummyproducts.features.list.presentation.vo.ListState
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineViewModel
import kotlinx.coroutines.flow.StateFlow
import logcat.logcat

class ListViewModel(
    private val pageStateRepository: PageStateRepository,
) : AbstractCoroutineViewModel(), UiEventDispatcher {
    fun observeState(): StateFlow<ListState> = pageStateRepository.observeState()

    init {
        setPage(1)
    }

    private fun setPage(page: Int) {
        pageStateRepository.setPage(page)
    }

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.ChangePage -> setPage(event.num)
            is UiEvent.OpenDetails -> logcat { "No details for id-${event.id} yet!" }
        }
    }
}

