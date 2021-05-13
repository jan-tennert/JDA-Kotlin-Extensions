package de.jan.jdaktx.utils

import de.jan.jdaktx.classes.utils.KPresence
import net.dv8tion.jda.api.entities.*
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
import net.dv8tion.jda.internal.utils.Checks


fun Message.onReactionAdd(
    predicate: (MessageReactionAddEvent) -> Boolean = { true },
    onReaction: (MessageReactionAddEvent) -> Unit
) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<MessageReactionAddEvent> {
        if (predicate(it) && it.messageId == this.id) {
            onReaction(it)
        }
    }
}

fun Message.onReactionRemove(
    predicate: (MessageReactionRemoveEvent) -> Boolean = { true },
    onReaction: (MessageReactionRemoveEvent) -> Unit
) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<MessageReactionRemoveEvent> {
        if (predicate(it) && it.messageId == this.id) {
            onReaction(it)
        }
    }
}

fun Message.onDeletion(onDelete: (MessageDeleteEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<MessageDeleteEvent> {
        if (it.messageId == this.id) {
            onDelete(it)
        }
    }
}

fun VoiceChannel.onMemberJoin(
    predicate: (GuildVoiceJoinEvent) -> Boolean = { true },
    onJoin: (GuildVoiceJoinEvent) -> Unit
) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<GuildVoiceJoinEvent> {
        if (it.channelJoined.id == this.id && predicate(it)) {
            onJoin(it)
        }
    }
}

fun VoiceChannel.onMemberLeave(
    predicate: (GuildVoiceLeaveEvent) -> Boolean = { true },
    onLeft: (GuildVoiceLeaveEvent) -> Unit
) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<GuildVoiceLeaveEvent> {
        if (it.channelLeft.id == this.id && predicate(it)) {
            onLeft(it)
        }
    }
}

fun VoiceChannel.onDeletion(onDelete: (VoiceChannelDeleteEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<VoiceChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

fun TextChannel.onDeletion(onDelete: (TextChannelDeleteEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<TextChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

fun TextChannel.onMessage(onMessage: (GuildMessageReceivedEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<GuildMessageReceivedEvent> {
        if (it.channel.id == this.id) {
            onMessage(it)
        }
    }
}

fun Category.onDeletion(onDelete: (CategoryDeleteEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<CategoryDeleteEvent> {
        if (it.category.id == this.id) {
            onDelete(it)
        }
    }
}

fun Guild.onMemberJoin(onJoin: (GuildMemberJoinEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<GuildMemberJoinEvent> {
        if (it.guild.id == this.id) {
            onJoin(it)
        }
    }
}

fun Guild.onMemberLeave(onLeave: (GuildMemberRemoveEvent) -> Unit) {
    Checks.check(
        jda.eventManager is KEventManager,
        "You need to set the Event Manager to KEventManager in order to use custom events"
    )
    val manager = jda.eventManager as KEventManager
    manager.on<GuildMemberRemoveEvent> {
        if (it.guild.id == this.id) {
            onLeave(it)
        }
    }
}

fun Presence.changeActivity(presence: KPresence.() -> Unit) {
    val p = KPresence(this)
    p.presence()
}
