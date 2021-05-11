package de.jan.jdaktx.CommandHandler.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

class KSubCommandGroup(var name: String, var description: String) {

    private val commands = mutableListOf<SubcommandData>()

    fun subCommands(cmd: KSubCommands.() -> Unit) {
        val subCommand = KSubCommands()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.commands.add(command)
        }
    }

    fun build(): SubcommandGroupData {
        val c = SubcommandGroupData(name, description)
        for (command in commands) {
            c.addSubcommand(command)
        }
        return c
    }

}