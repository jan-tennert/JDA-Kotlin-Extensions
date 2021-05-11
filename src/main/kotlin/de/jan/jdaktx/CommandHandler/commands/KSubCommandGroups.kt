package de.jan.jdaktx.CommandHandler.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

class KSubCommandGroups {

    val commands = mutableListOf<SubcommandGroupData>()

    fun subCommandGroup(name: String = "", description: String = "", cmd: KSubCommandGroup.() -> Unit) {
        val subCommand = KSubCommandGroup(name, description)
        subCommand.cmd()
        commands.add(subCommand.build())
    }

}