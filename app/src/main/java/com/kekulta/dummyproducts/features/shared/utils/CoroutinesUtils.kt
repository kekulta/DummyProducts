package com.kekulta.dummyproducts.features.shared.utils

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


private class TransformedStateFlow<T>(
    private val getValue: () -> T, private val flow: Flow<T>
) : StateFlow<T> {

    override val replayCache: List<T> get() = listOf(value)
    override val value: T get() = getValue()

    override suspend fun collect(collector: FlowCollector<T>): Nothing =
        coroutineScope { flow.stateIn(this).collect(collector) }
}

/**
 * Returns [StateFlow] from [flow] having initial value from calculation of [getValue]
 */
fun <T> stateFlow(
    getValue: () -> T, flow: Flow<T>
): StateFlow<T> = TransformedStateFlow(getValue, flow)

/**
 * Combines all [stateFlows] and transforms them into another [StateFlow] with [transform]
 */
inline fun <reified T, R> combineStatesInternal(
    vararg stateFlows: StateFlow<T>, crossinline transform: (Array<T>) -> R
): StateFlow<R> = stateFlow(getValue = { transform(stateFlows.map { it.value }.toTypedArray()) },
    flow = combine(*stateFlows) { transform(it) })

/**
 * Variant of [combineStates] for combining 3 state flows
 */
inline fun <reified T1, reified T2, reified T3, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    crossinline transform: (T1, T2, T3) -> R
) = combineStatesInternal(flow1, flow2, flow3) { (t1, t2, t3) ->
    transform(
        t1 as T1, t2 as T2, t3 as T3
    )
}

/**
 * Variant of [combineStates] for combining 4 state flows
 */
inline fun <reified T1, reified T2, reified T3, reified T4, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    crossinline transform: (T1, T2, T3, T4) -> R
) = combineStatesInternal(flow1, flow2, flow3, flow4) { (t1, t2, t3, t4) ->
    transform(
        t1 as T1, t2 as T2, t3 as T3, t4 as T4
    )
}

/**
 * Variant of [combineStates] for combining 5 state flows
 */
inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    flow5: StateFlow<T5>,
    crossinline transform: (T1, T2, T3, T4, T5) -> R
) = combineStatesInternal(flow1, flow2, flow3, flow4, flow5) { (t1, t2, t3, t4, t5) ->
    transform(
        t1 as T1, t2 as T2, t3 as T3, t4 as T4, t5 as T5
    )
}

/**
 * Does not produce the same value in a raw, so respect "distinct until changed emissions"
 * */
class DerivedStateFlow<T>(
    private val getValue: () -> T, private val flow: Flow<T>
) : StateFlow<T> {

    override val replayCache: List<T>
        get() = listOf(value)

    override val value: T
        get() = getValue()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        coroutineScope { flow.distinctUntilChanged().stateIn(this).collect(collector) }
    }
}

fun <T1, R> StateFlow<T1>.mapState(transform: (a: T1) -> R): StateFlow<R> {
    return DerivedStateFlow(getValue = { transform(this.value) },
        flow = this.map { a -> transform(a) })
}

fun <T1, T2, R> combineStates(
    flow: StateFlow<T1>, flow2: StateFlow<T2>, transform: (a: T1, b: T2) -> R
): StateFlow<R> {
    return DerivedStateFlow(getValue = { transform(flow.value, flow2.value) },
        flow = combine(flow, flow2) { a, b -> transform(a, b) })
}
