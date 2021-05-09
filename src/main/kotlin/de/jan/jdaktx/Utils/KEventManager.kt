package de.jan.jdaktx.Utils

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
        for (listener in listeners) {
            listener.onEvent(e)
        }
    }

    override fun getRegisteredListeners(): MutableList<EventListener> {
        return listeners
    }

    inline fun <reified T : Event> on(crossinline event: (T) -> Unit) {
        val listener = EventListener {
            if (it is T) {
                event.invoke(it)
            }
        }
        listeners.add(listener)
    }

}
