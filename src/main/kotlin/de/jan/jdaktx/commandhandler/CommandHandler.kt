package de.jan.jdaktx.commandhandler

import de.jan.jdaktx.eventmanager.eventScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import net.dv8tion.jda.internal.utils.Checks

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
        val global = jda.updateCommands()
        val guild = hashMapOf<Long, CommandListUpdateAction>()
        this.commands.addAll(commands)
        var index = 0
        for (command in commands) {
            if (!command.autoRegister) continue
            index++
            if (command.guildID != null) {
                if (guild.containsKey(command.guildID)) {
                    guild[command.guildID]!!.addCommands(command)
                } else {
                    val g = jda.getGuildById(command.guildID!!)
                    Checks.notNull(g, "Bot Guild (${command.guildID})")
                    guild[command.guildID!!] = g!!.updateCommands().addCommands(command)
                }
            } else {
                global.addCommands(command)
            }
        }
        global.queue()
        for (i in guild) {
            i.value.queue()
        }
    }
}

fun JDA.createCommandHandler(): CommandHandler {
    return CommandHandler(this)
}