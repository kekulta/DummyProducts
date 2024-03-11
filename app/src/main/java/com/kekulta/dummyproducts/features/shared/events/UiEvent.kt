package com.kekulta.dummyproducts.features.shared.events

import com.kekulta.dummyproducts.features.list.domain.models.AlertMessage

sealed class UiEvent() {
    data class Message(val mes: AlertMessage) : UiEvent()
    data class PreviewLoaded(val id: Int) : UiEvent()
    data class PageClicked(val num: Int) : UiEvent()
    data class PreviewClicked(val id: Int) : UiEvent()
}