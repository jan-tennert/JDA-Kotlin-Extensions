package de.jan.jdaktx.commandhandler.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

class KSubCommands {

    internal val commands = mutableListOf<SubcommandData>()

    fun subCommand(name: String = "", description: String = "", cmd: KSubCommand.() -> Unit = {}) {
        val subCommand = KSubCommand(name, description)
        subCommand.cmd()
        commands.add(subCommand.subCommand)
    }

}