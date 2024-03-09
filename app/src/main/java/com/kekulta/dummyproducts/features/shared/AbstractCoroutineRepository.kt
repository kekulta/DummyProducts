package com.kekulta.dummyproducts.features.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class AbstractCoroutineRepository {
    private val holder = mutableMapOf<Any, Job>()
    private val managerScope = CoroutineScope(Dispatchers.Main)

    fun launchScope(
        scopeId: Any,
        rerunType: RerunType = RerunType.RESTART,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (holder[scopeId]?.isActive == true && rerunType == RerunType.KEEP) {
            return
        }
        cancelScope(scopeId)
        holder[scopeId] = managerScope.launch(dispatcher) {
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

    fun clear() {
        managerScope.cancel()
        holder.clear()
    }
}