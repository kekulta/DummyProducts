package com.kekulta.dummyproducts.features.shared.events

interface UiEventDispatcher {
    fun dispatch(event: UiEvent)
    fun attachUiEventProvider(provider: UiEventProvider) {
        provider.uiEventCallback = ::dispatch
    }
}