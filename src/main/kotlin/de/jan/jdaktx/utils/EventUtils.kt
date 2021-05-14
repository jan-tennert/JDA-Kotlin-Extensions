package de.jan.jdaktx.utils

import de.jan.jdaktx.classes.utils.KPresence
import de.jan.jdaktx.eventmanager.on
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

/**
 * Called when a reaction is added to the message
 */
fun Message.onReactionAdd(
    predicate: (MessageReactionAddEvent) -> Boolean = { true },
    onReaction: (MessageReactionAddEvent) -> Unit
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
    onReaction: (MessageReactionRemoveEvent) -> Unit
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
fun Message.onDeletion(onDelete: (MessageDeleteEvent) -> Unit) {
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
    onJoin: (GuildVoiceJoinEvent) -> Unit
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
    onLeft: (GuildVoiceLeaveEvent) -> Unit
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
fun VoiceChannel.onDeletion(onDelete: (VoiceChannelDeleteEvent) -> Unit) {
    jda.on<VoiceChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when this text channel is deleted
 */
fun TextChannel.onDeletion(onDelete: (TextChannelDeleteEvent) -> Unit) {
    jda.on<TextChannelDeleteEvent> {
        if (it.channel.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when someone enters a message in this channel
 */
fun TextChannel.onMessage(onMessage: (GuildMessageReceivedEvent) -> Unit) {
    jda.on<GuildMessageReceivedEvent> {
        if (it.channel.id == this.id) {
            onMessage(it)
        }
    }
}

/**
 * Called when someone deletes this category
 */
fun Category.onDeletion(onDelete: (CategoryDeleteEvent) -> Unit) {
    jda.on<CategoryDeleteEvent> {
        if (it.category.id == this.id) {
            onDelete(it)
        }
    }
}

/**
 * Called when someone joins this guild
 */
fun Guild.onMemberJoin(onJoin: (GuildMemberJoinEvent) -> Unit) {
    jda.on<GuildMemberJoinEvent> {
        if (it.guild.id == this.id) {
            onJoin(it)
        }
    }
}

/**
 * Called when someone leaves this guild
 */
fun Guild.onMemberLeave(onLeave: (GuildMemberRemoveEvent) -> Unit) {
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
