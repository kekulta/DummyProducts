package com.kekulta.dummyproducts.features.list.presentation.ui

import com.kekulta.dummyproducts.features.shared.events.UiEventCallback
import com.kekulta.dummyproducts.features.shared.events.UiEventProvider

class Dispatcher : UiEventProvider {
    override var uiEventCallback: UiEventCallback? = null
}