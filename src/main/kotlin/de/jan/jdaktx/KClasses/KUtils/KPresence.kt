package de.jan.jdaktx.KClasses.KUtils

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.managers.Presence
import net.dv8tion.jda.internal.utils.Checks

class KPresence(private val presence: Presence) {

    var onlineStatus = presence.status
        set(value) {
            field = value
            this.presence.setStatus(value)
        }

    fun playing(text: String) {
        presence.activity = Activity.playing(text)
    }

    fun listening(text: String) {
        presence.activity = Activity.listening(text)
    }

    fun streaming(text: String, link: String?) {
        Checks.check(Activity.isValidStreamingUrl(link), "You need to specify a valid streaming url")
        presence.activity = Activity.streaming(text, link)
    }

    fun watching(text: String) {
        presence.activity = Activity.watching(text)
    }

    fun competing(text: String) {
        presence.activity = Activity.competing(text)
    }

    fun online() {
        presence.setStatus(OnlineStatus.ONLINE)
    }

    fun dnd() {
        presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
    }

    fun idle() {
        presence.setStatus(OnlineStatus.IDLE)
    }

    fun invisible() {
        presence.setStatus(OnlineStatus.INVISIBLE)
    }

    fun offline() {
        presence.setStatus(OnlineStatus.OFFLINE)
    }

}