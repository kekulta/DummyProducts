package com.kekulta.dummyproducts.features.shared.events

import logcat.logcat

interface UiEventProvider {
    var uiEventCallback: UiEventCallback?

    fun dispatch(event: UiEvent) {
        if (uiEventCallback == null) {
            logcat { "Lost event: $event" }
        }
        uiEventCallback?.invoke(event)
    }
}