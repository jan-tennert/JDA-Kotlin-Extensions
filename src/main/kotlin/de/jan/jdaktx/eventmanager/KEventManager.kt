package de.jan.jdaktx.eventmanager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.IEventManager

class KEventManager : IEventManager {

    private val listeners = mutableListOf<Any>()

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
        GlobalScope.launch {
            val iterator = listeners.iterator()
            while (iterator.hasNext()) {
                when (val next = iterator.next()) {
                    is KEventListener -> {
                        if (next.maxRuns == -1) {
                            next.onEvent(e)
                        } else if (next.maxRuns > 0) {
                            if (next.onEvent(e)) {
                                next.maxRuns--
                            }
                        }
                    }
                    is EventListener -> next.onEvent(e)
                }
            }
        }
    }

    override fun getRegisteredListeners(): MutableList<Any> {
        return listeners
    }

}

inline fun <reified T : GenericEvent> JDA.on(maxRuns: Int = -1, crossinline event: suspend (T) -> Unit) {
    if (!this.hasKotlinExtensions) setupKotlinExtensions()
    val listener = object : KEventListener(maxRuns) {
        override suspend fun onEvent(e: GenericEvent): Boolean {
            if (e is T) {
                event.invoke(e)
                return true
            }
            return false
        }
    }
    this.addEventListener(listener)
}

fun JDA.setupKotlinExtensions() {
    this.setEventManager(KEventManager())
}

abstract class KEventListener(var maxRuns: Int) {
    abstract suspend fun onEvent(e: GenericEvent): Boolean
}

val JDA.hasKotlinExtensions: Boolean
    get() = eventManager is KEventManager