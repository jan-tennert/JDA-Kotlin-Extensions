package de.jan.jdaktx.CommandHandler.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

class KSubCommands {

    val commands = mutableListOf<SubcommandData>()

    fun subCommand(name: String = "", description: String = "", cmd: KSubCommand.() -> Unit) {
        val subCommand = KSubCommand(name, description)
        subCommand.cmd()
        commands.add(subCommand.subCommand)
    }

}