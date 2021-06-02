package de.jan.jdaktx.utils

import de.jan.jdaktx.eventmanager.awaitEvent
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent

suspend fun MessageChannel.awaitMessage(timeout: Long, predicate: (Message) -> Boolean = { true }): Message? {
    return jda.awaitEvent<MessageReceivedEvent>(
        timeout,
        predicate = { e -> e.channel.id == this.id && predicate(e.message) })?.message
}

suspend fun MessageChannel.awaitMessage(predicate: (Message) -> Boolean = { true }): Message {
    return jda.awaitEvent<MessageReceivedEvent>(predicate = { e -> e.channel.id == this.id && predicate(e.message) }).message
}

suspend fun MessageChannel.awaitMessages(count: Int, predicate: (Message) -> Boolean = { true }): List<Message> {
    val messages = mutableListOf<Message>()
    for (i in 1..count) {
        val msg = awaitMessage(predicate)
        messages.add(msg)
    }
    return messages.toList()
}

suspend fun MessageChannel.awaitMessages(count: Int, timeout: Long, predicate: (Message) -> Boolean = { true }) =
    withTimeoutOrNull(timeout) {
        val messages = mutableListOf<Message>()
        for (i in 1..count) {
            val msg = awaitMessage(predicate)
            messages.add(msg)
        }
        messages.toList()
    }

suspend fun Message.awaitReaction(predicate: (MessageReaction) -> Boolean = { true }): MessageReaction {
    return jda.awaitEvent<MessageReactionAddEvent> { it.messageId == this.id && predicate(it.reaction) }.reaction
}

suspend fun Message.awaitReaction(timeout: Long, predicate: (MessageReaction) -> Boolean = { true }) =
    withTimeoutOrNull(timeout) {
        jda.awaitEvent<MessageReactionAddEvent> { it.messageId == id && predicate(it.reaction) }.reaction
    }

