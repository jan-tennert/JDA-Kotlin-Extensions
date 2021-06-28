package de.jan.jdaktx.commandhandler

import de.jan.jdaktx.commandhandler.commands.KOptions
import de.jan.jdaktx.commandhandler.commands.KSubCommandGroups
import de.jan.jdaktx.commandhandler.commands.KSubCommands
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.internal.utils.Checks

abstract class Command(
    name: String,
    description: String,
    var guildID: Long? = null,
    var autoRegister: Boolean = true
) : CommandData(name, description) {

    open fun init() {}

    abstract suspend fun run(
        channel: TextChannel?,
        member: Member?,
        user: User,
        privateChannel: PrivateChannel?,
        hook: InteractionHook,
        options: MutableList<OptionMapping>,
        event: SlashCommandEvent
    )

}

class ImplementedCommand {

    private val command = KCommand()
    private var action: SlashCommandRun? = null
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


    fun action(action: SlashCommandRun) {
        this.action = action
    }

    fun options(options: KOptions.() -> Unit) {
        val ops = KOptions()
        ops.options()
        for (option in ops.options) {
            command.addOptions(option)
        }
    }

    fun subCommands(cmd: KSubCommands.() -> Unit) {
        val subCommand = KSubCommands()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.command.addSubcommands(command)
        }
    }

    fun subCommandGroups(cmd: KSubCommandGroups.() -> Unit) {
        val subCommand = KSubCommandGroups()
        subCommand.cmd()
        for (command in subCommand.commands) {
            this.command.addSubcommandGroups(command)
        }
    }

    internal fun build(): Command {
        Checks.check(name.isNotBlank(), "A command requires a name")
        Checks.check(description.isNotBlank(), "A command requires a description")
        return command
    }

    private class KCommand : Command("empty", "empty") {

        var action: SlashCommandRun? = null

        override suspend fun run(
            channel: TextChannel?,
            member: Member?,
            user: User,
            privateChannel: PrivateChannel?,
            hook: InteractionHook,
            options: MutableList<OptionMapping>,
            event: SlashCommandEvent
        ) {
            action?.handle(event)
        }

    }

}

fun interface SlashCommandRun {
    suspend fun handle(event: SlashCommandEvent)
}

fun Command.isTheSameAs(other: net.dv8tion.jda.api.interactions.commands.Command): Boolean {
    return other.name == name && other.description == description && other.options == getOptions() && other.subcommands == subcommands && other.subcommandGroups == subcommandGroups
}
