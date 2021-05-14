package de.jan.jdaktx.commandhandler

import de.jan.jdaktx.commandhandler.commands.KOptions
import de.jan.jdaktx.commandhandler.commands.KSubCommandGroups
import de.jan.jdaktx.commandhandler.commands.KSubCommands
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.CommandHook
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.utils.Checks

abstract class Command(
    name: String,
    description: String,
    var guildID: Long? = null,
    var autoRegister: Boolean = true
) : CommandData(name, description) {

    abstract fun run(
        channel: TextChannel?,
        member: Member?,
        user: User,
        privateChannel: PrivateChannel?,
        hook: CommandHook,
        options: MutableList<SlashCommandEvent.OptionData>,
        event: SlashCommandEvent
    )

}

class ImplementedCommand {

    private val command = KCommand()
    private var action: ((SlashCommandEvent) -> Unit)? = null
        set(value) {
            field = value
            command.action = value
        }
    var name: String = ""
        set(value) {
            field = value
            command.name = value
        }
    var description: String = ""
        set(value) {
            field = value
            command.description = value
        }
    var autoRegister: Boolean = true
        set(value) {
            field = value
            command.autoRegister = value
        }
    var guildID: Long? = null
        set(value) {
            field = value
            command.guildID = value
        }


    fun action(action: (SlashCommandEvent) -> Unit) {
        this.action = action
    }

    fun options(options: KOptions.() -> Unit) {
        val ops = KOptions()
        ops.options()
        for (option in ops.options) {
            command.addOption(option)
        }
    }

    fun subCommands(cmd: KSubCommands.() -> Unit) {
        val subCommand = KSubCommands()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.command.addSubcommand(command)
        }
    }

    fun subCommandGroups(cmd: KSubCommandGroups.() -> Unit) {
        val subCommand = KSubCommandGroups()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.command.addSubcommandGroup(command)
        }
    }

    internal fun build(): Command {
        Checks.check(name.isNotBlank(), "A command requires a name")
        Checks.check(description.isNotBlank(), "A command requires a description")
        return command
    }

    private class KCommand : Command("empty", "empty") {

        var action: ((SlashCommandEvent) -> Unit)? = null

        override fun run(
            channel: TextChannel?,
            member: Member?,
            user: User,
            privateChannel: PrivateChannel?,
            hook: CommandHook,
            options: MutableList<SlashCommandEvent.OptionData>,
            event: SlashCommandEvent
        ) {
            action?.invoke(event)
        }

    }

}

/**
 * Creates a new slash command through a type safe way.
 * name and description are required
 */
fun createSlashCommand(command: ImplementedCommand.() -> Unit): Command {
    val cmd = ImplementedCommand()
    cmd.command()
    return cmd.build()
}