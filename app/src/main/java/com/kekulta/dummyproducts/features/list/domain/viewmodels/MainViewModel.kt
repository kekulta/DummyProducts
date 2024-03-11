package com.kekulta.dummyproducts.features.list.domain.viewmodels

import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage
import com.kekulta.dummyproducts.features.list.presentation.ui.Dispatcher
import com.kekulta.dummyproducts.features.shared.AbstractCoroutineViewModel
import com.kekulta.dummyproducts.features.shared.events.UiEvent
import com.kekulta.dummyproducts.features.shared.events.UiEventDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import logcat.logcat

class MainViewModel(
    dispatcher: Dispatcher,
) : AbstractCoroutineViewModel(), UiEventDispatcher {
    init {
        dispatcher.uiEventCallback = ::dispatch
    }

    private val channel = Channel<AlertMessage>()
    fun observeMessages(): Flow<AlertMessage> = channel.receiveAsFlow().distinctUntilChanged()
    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.Message -> {
                launchScope(System.currentTimeMillis()) { channel.send(event.mes) }
            }

            else -> {
                logcat { "Unhandled event: $event" }
            }
        }
    }
}

