package de.jan.jdaktx.eventmanager

import kotlinx.coroutines.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.IEventManager

class KEventManager : IEventManager {

    private val listeners = mutableListOf<Any>()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun register(e: Any) {
        if (e is EventListener || e is KEventListener) {
            listeners.add(e)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun unregister(e: Any) {
        if (e is EventListener || e is KEventListener) {
            listeners.remove(e)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun handle(e: GenericEvent) {
        scope.launch {
            val listenersCopy = listeners.toList()
            val iterator = listenersCopy.iterator()
            while (iterator.hasNext()) {
                when (val next = iterator.next()) {
                    is KEventListener -> next.onEvent(e)
                    is EventListener -> next.onEvent(e)
                }
            }
        }
    }

    override fun getRegisteredListeners(): MutableList<Any> {
        return listeners
    }

}

inline fun <reified T : GenericEvent> JDA.on(
    crossinline predicate: (T) -> Boolean = { true },
    crossinline event: suspend (T) -> Unit
) {
    if (!this.hasKotlinExtensions) setupKotlinExtensions()
    val listener = object : KEventListener {
        override suspend fun onEvent(e: GenericEvent) {
            if (e is T) {
                if (predicate(e)) {
                    event.invoke(e)
                }
            }
        }
    }
    this.addEventListener(listener)
}

suspend inline fun <reified T : GenericEvent> JDA.awaitEvent(crossinline predicate: (T) -> Boolean = { true }) =
    suspendCancellableCoroutine<T> {
        if (!this.hasKotlinExtensions) setupKotlinExtensions()
        val listener = object : KEventListener {
            override suspend fun onEvent(e: GenericEvent) {
                if (e is T && predicate(e)) {
                    removeEventListener(this)
                    it.resume(e) { error ->
                        error.printStackTrace()
                    }
                }
            }
        }
        this.addEventListener(listener)
        it.invokeOnCancellation { removeEventListener(listener) }
    }

suspend inline fun <reified T : GenericEvent> JDA.awaitEvent(
    timeoutInMilliseconds: Long,
    crossinline predicate: (T) -> Boolean = { true }
) = withTimeoutOrNull(timeoutInMilliseconds) {
    awaitEvent(predicate)
}

fun JDA.setupKotlinExtensions() {
    this.setEventManager(KEventManager())
}

interface KEventListener {
    suspend fun onEvent(e: GenericEvent)
}

val JDA.hasKotlinExtensions: Boolean
    get() = eventManager is KEventManager

