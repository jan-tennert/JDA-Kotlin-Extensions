package de.jan.jdaktx.commandhandler.commands

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import net.dv8tion.jda.internal.utils.Checks

class KSubCommandGroup(var name: String, var description: String) {

    private val commands = mutableListOf<SubcommandData>()

    fun subCommands(cmd: KSubCommands.() -> Unit) {
        val subCommand = KSubCommands()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.commands.add(command)
        }
    }

    internal fun build(): SubcommandGroupData {
        Checks.check(name.isNotBlank(), "A Subcommandgroup requires a name")
        Checks.check(description.isNotBlank(), "A Subcommandgroup requires a description")
        val c = SubcommandGroupData(name, description)
        for (command in commands) {
            c.addSubcommands(command)
        }
        return c
    }

}