package de.jan.jdaktx.CommandHandler

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction

class CommandHandler(val jda: JDA) : ListenerAdapter() {

    private val commands = mutableListOf<Command>()

    init {
        jda.addEventListener(this)
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        for (command in commands) {
            if(event.name == command.name) {
                if(event.member != null) {
                    command.run(if(event.isFromGuild) event.textChannel else null, event.member, event.user, if(!event.isFromGuild) event.privateChannel else null, event.hook, event.options, event)
                } else {
                    command.run(if(event.isFromGuild) event.textChannel else null, event.member, event.user, if(!event.isFromGuild) event.privateChannel else null, event.hook, event.options, event)
                }
            }
        }
    }

    fun registerCommands(vararg commands: Command) {
        val global = jda.updateCommands()
        val guild = hashMapOf<Long, CommandUpdateAction>()
        this.commands.addAll(commands)
        var index = 0
        for (command in commands) {
            if (!command.autoRegister) continue
            index++
            if(command.guildID != null) {
                if(guild.containsKey(command.guildID)) {
                    guild[command.guildID]!!.addCommands(command)
                } else {
                    guild[command.guildID!!] = jda.getGuildById(command.guildID!!)!!.updateCommands().addCommands(command)
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