package de.jan.jdaktx.eventmanager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.IEventManager

class KEventManager : IEventManager {

    @PublishedApi
    internal val listeners = mutableListOf<EventListener>()

    override fun register(e: Any) {
        if (e is EventListener) {
            listeners.add(e)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun unregister(e: Any) {
        if (e is EventListener) {
            listeners.remove(e)
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun handle(e: GenericEvent) {
        val iterator = listeners.iterator()
        while (iterator.hasNext()) {
            iterator.next().onEvent(e)
        }
    }

    override fun getRegisteredListeners(): MutableList<EventListener> {
        return listeners
    }

}

inline fun <reified T : Event> JDA.on(crossinline event: suspend (T) -> Unit) {
    if (!this.hasKotlinExtensions) setupKotlinExtensions()
    val listener = EventListener {
        if (it is T) {
            GlobalScope.launch {
                event.invoke(it)
            }
        }
    }
    this.addEventListener(listener)
}

fun JDA.setupKotlinExtensions() {
    this.setEventManager(KEventManager())
}

val JDA.hasKotlinExtensions: Boolean
    get() = eventManager is KEventManager