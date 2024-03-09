package com.kekulta.dummyproducts.features.shared.events

sealed class UiEvent() {
    data class ChangePage(val num: Int) : UiEvent()
    data class OpenDetails(val id: Int) : UiEvent()
}