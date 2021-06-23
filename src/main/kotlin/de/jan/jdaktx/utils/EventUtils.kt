package de.jan.jdaktx.utils

import de.jan.jdaktx.classes.utils.KPresence
import de.jan.jdaktx.eventmanager.on
import kotlinx.coroutines.suspendCancellableCoroutine
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.managers.Presence
import net.dv8tion.jda.api.requests.RestAction

/**
 * Called when a reaction is added to the message
 */
fun Message.onReactionAdd(
    predicate: (MessageReactionAddEvent) -> Boolean = { true },
    onReaction: suspend (MessageReactionAddEvent) -> Unit
) {
    jda.on<MessageReactionAddEvent> {
        if (predicate(it) && it.messageId == this.id) {
            onReaction(it)
        }
    }
}

/**
 * Called when a reaction is removed from the message
 */
fun Message.onReactionRemove(
    predicate: (MessageReactionRemoveEvent) -> Boolean = { true },
    onReaction: suspend (MessageReactionRemoveEvent) -> Unit
) {
    jda.on<MessageReactionRemoveEvent> {
        if (predicate(it) && it.messageId == this.id) {
            onReaction(it)
        }
    }
}

/**
 * Called when the message is deleted
 */
fun Message.onDeletion(onDelete: suspend (MessageDeleteEvent) -> Unit) {
    jda.on<MessageDeleteEvent> {
        if (it.messageId == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when a member joins the voice channel
 */
fun VoiceChannel.onMemberJoin(
    predicate: (GuildVoiceJoinEvent) -> Boolean = { true },
    onJoin: suspend (GuildVoiceJoinEvent) -> Unit
) {
    jda.on<GuildVoiceJoinEvent> {
        if (it.channelJoined.id == this.id && predicate(it)) {
            onJoin(it)
        }
    }
}

/**
 * Called when a member leaves this voice channel
 */
fun VoiceChannel.onMemberLeave(
    predicate: (GuildVoiceLeaveEvent) -> Boolean = { true },
    onLeft: suspend (GuildVoiceLeaveEvent) -> Unit
) {
    jda.on<GuildVoiceLeaveEvent> {
        if (it.channelLeft.id == this.id && predicate(it)) {
            onLeft(it)
        }
    }
}

/**
 * Called when this voice channel is deleted
 */
fun VoiceChannel.onDeletion(onDelete: suspend (VoiceChannelDeleteEvent) -> Unit) {
    jda.on<VoiceChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when this text channel is deleted
 */
fun TextChannel.onDeletion(onDelete: suspend (TextChannelDeleteEvent) -> Unit) {
    jda.on<TextChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when someone enters a message in this channel
 */
fun TextChannel.onMessage(onMessage: suspend (GuildMessageReceivedEvent) -> Unit) {
    jda.on<GuildMessageReceivedEvent> {
        if (it.channel.id == this.id) {
            onMessage(it)
        }
    }
}

/**
 * Called when someone deletes this category
 */
fun Category.onDeletion(onDelete: suspend (CategoryDeleteEvent) -> Unit) {
    jda.on<CategoryDeleteEvent> {
        if (it.category.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when someone joins this guild
 */
fun Guild.onMemberJoin(onJoin: suspend (GuildMemberJoinEvent) -> Unit) {
    jda.on<GuildMemberJoinEvent> {
        if (it.guild.id == this.id) {
            onJoin(it)
        }
    }
}

/**
 * Called when someone leaves this guild
 */
fun Guild.onMemberLeave(onLeave: suspend (GuildMemberRemoveEvent) -> Unit) {
    jda.on<GuildMemberRemoveEvent> {
        if (it.guild.id == this.id) {
            onLeave(it)
        }
    }
}

/**
 * Change the activity and the online status of the bot through a type safe way
 */
fun Presence.changeActivity(presence: KPresence.() -> Unit) {
    val p = KPresence(this)
    p.presence()
}

fun <T : GenericEvent> JDA.fireEvent(event: T) {
    eventManager.handle(event)
}

suspend fun <T> RestAction<T>.await() = suspendCancellableCoroutine<T> {
    queue() { re -> it.resume(re) { it.printStackTrace() } }
}