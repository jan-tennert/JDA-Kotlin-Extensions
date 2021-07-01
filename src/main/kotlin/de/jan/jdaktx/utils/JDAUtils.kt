package de.jan.jdaktx.utils

import de.jan.jdaktx.commandhandler.Command
import de.jan.jdaktx.eventmanager.on
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageHistory
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.utils.TimeFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

suspend fun MessageChannel.purgeMessages(amount: Int): CompletableFuture<Void> {
    val history = MessageHistory(this)
    val messages = history.retrievePast(amount).await()
    return CompletableFuture.allOf(*purgeMessages(messages).toTypedArray())
}

fun JDA.onSlashCommand(command: String, on: (SlashCommandEvent) -> Unit) =
    on<SlashCommandEvent>(predicate = { it.name == command }) {
        on(it)
    }

fun JDA.onSlashCommand(command: Command, on: ((SlashCommandEvent) -> Unit)? = null) =
    on<SlashCommandEvent>(predicate = {
        it.name == command.name || it.name == command.name && (it.isFromGuild && command.guildID != null && it.guild!!.idLong == command.guildID)
    }) {
        if (on != null) {
            on(it)
        } else {
            command.run(
                (if (it.isFromGuild) it.textChannel else null),
                (if (it.isFromGuild) it.member else null),
                it.user,
                (if (!it.isFromGuild) it.privateChannel else null),
                it.hook,
                it.options,
                it
            )
        }
    }

fun Long.format(format: TimeFormat) = format.format(this)

@ExperimentalTime
fun Duration.afterNow(format: TimeFormat) =
    format.format(Instant.now().plus(this.inWholeMilliseconds, ChronoUnit.MILLIS))

@ExperimentalTime
fun Duration.beforeNow(format: TimeFormat) =
    format.format(Instant.now().minus(this.inWholeMilliseconds, ChronoUnit.MILLIS))