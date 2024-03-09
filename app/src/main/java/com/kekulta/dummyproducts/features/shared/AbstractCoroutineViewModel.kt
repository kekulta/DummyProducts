package com.kekulta.dummyproducts.features.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class AbstractCoroutineViewModel : ViewModel() {
    private val holder = mutableMapOf<Any, Job>()

    fun launchScope(
        scopeId: Any = this,
        rerunType: RerunType = RerunType.RESTART,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (holder[scopeId]?.isActive == true && rerunType == RerunType.KEEP) {
            return
        }
        cancelScope(scopeId)
        holder[scopeId] = viewModelScope.launch(dispatcher) {
            block()
        }
    }

    fun cancelScope(scopeId: Any): Boolean {
        return if (holder[scopeId] == null) {
            false
        } else {
            holder[scopeId]?.cancel()
            true
        }
    }

    override fun onCleared() {
        super.onCleared()
        holder.forEach { (_, job) -> job.cancel() }
    }
}