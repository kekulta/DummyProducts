package com.kekulta.dummyproducts.features.shared.events

interface UiEventProvider {
    var uiEventCallback: UiEventCallback?

    fun dispatch(event: UiEvent) {
        uiEventCallback?.invoke(event)
    }
}