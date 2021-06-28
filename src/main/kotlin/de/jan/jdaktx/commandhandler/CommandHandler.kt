package de.jan.jdaktx.commandhandler

import de.jan.jdaktx.eventmanager.KEventManager
import de.jan.jdaktx.eventmanager.eventScope
import de.jan.jdaktx.utils.await
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.system.measureTimeMillis

class CommandHandler(val jda: JDA) : ListenerAdapter() {

    private val commands = mutableListOf<Command>()

    init {
        jda.addEventListener(this)
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        for (command in commands) {
            if (event.name == command.name) {
                jda.eventScope.launch {
                    if (event.member != null) {
                        command.run(
                            if (event.isFromGuild) event.textChannel else null,
                            event.member,
                            event.user,
                            if (!event.isFromGuild) event.privateChannel else null,
                            event.hook,
                            event.options,
                            event
                        )
                    } else {
                        command.run(
                            if (event.isFromGuild) event.textChannel else null,
                            event.member,
                            event.user,
                            if (!event.isFromGuild) event.privateChannel else null,
                            event.hook,
                            event.options,
                            event
                        )
                    }
                }
            }
        }
    }

    /**
     * Register commands you created using [Command] or the [createSlashCommand] method
     */
    fun registerCommands(vararg commands: Command) {
        this.commands.addAll(commands)
        val time = measureTimeMillis {
            runBlocking {
                launch {
                    val globalCommands =
                        SlashCommandUpdate(commands.filter { it.guildID == null }.toMutableList(), jda = jda)
                    if (globalCommands.hasChanged()) {
                        globalCommands.update()
                        KEventManager.LOGGER.info("Updating ${globalCommands.commands.size} global commands")
                    }
                    delay(500)
                    val guildCommands = hashMapOf<Long, SlashCommandUpdate>()
                    commands.filter { it.guildID != null }.forEach {
                        if (!guildCommands.containsKey(it.guildID)) {
                            guildCommands[it.guildID!!] =
                                SlashCommandUpdate(mutableListOf(it), jda.getGuildById(it.guildID!!), jda)
                        } else {
                            guildCommands[it.guildID!!]!!.commands.add(it)
                        }
                    }
                    guildCommands.forEach { (id, update) ->
                        if (update.hasChanged()) {
                            update.update()
                            KEventManager.LOGGER.info("Updating ${update.commands.size} commands for $id")
                        }
                        delay(500)
                    }
                }
            }
        }
        KEventManager.LOGGER.info(
            "Registered all commands in ${
                BigDecimal((time.toDouble() / 1000)).setScale(
                    2,
                    RoundingMode.HALF_EVEN
                ).toDouble()
            } seconds!"
        )
    }
}

fun JDA.createCommandHandler(): CommandHandler {
    return CommandHandler(this)
}

class SlashCommandUpdate(val commands: MutableList<Command>, val guild: Guild? = null, val jda: JDA) {

    suspend fun hasChanged(): Boolean {
        if (guild == null) {
            val globalCommands = jda.retrieveCommands().await()
            commands.forEach { localCommand ->
                globalCommands.forEach { globalCommand ->
                    if (!localCommand.isTheSameAs(globalCommand)) {
                        return true
                    }
                }
            }
        } else {
            val guildCommands = guild.retrieveCommands().await()
            commands.forEach { localCommand ->
                guildCommands.forEach { guildCommand ->
                    if (!localCommand.isTheSameAs(guildCommand)) return true
                }
            }
        }
        return false
    }

    fun update() {
        if (guild == null) {
            jda.updateCommands().addCommands(commands).queue()
        } else {
            guild.updateCommands().addCommands(commands).queue()
        }
    }

}