package de.jan.jdaktx.utils

import de.jan.jdaktx.commandhandler.Command
import de.jan.jdaktx.commandhandler.ImplementedCommand
import de.jan.jdaktx.commandhandler.commands.KOption
import de.jan.jdaktx.commandhandler.commands.KSubCommand
import de.jan.jdaktx.commandhandler.commands.KSubCommandGroup
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import net.dv8tion.jda.internal.utils.Checks

/**
 * Creates a new slash command through a type safe way.
 * name and description are required
 */
fun createSlashCommand(command: ImplementedCommand.() -> Unit): Command {
    val cmd = ImplementedCommand()
    cmd.command()
    return cmd.build()
}

fun createOption(option: KOption.() -> Unit): OptionData {
    val op = KOption("", "", OptionType.UNKNOWN, false)
    op.option()
    Checks.check(op.name.isNotBlank(), "An option requires a name")
    Checks.check(op.description.isNotBlank(), "An option requires a description")
    Checks.check(op.type != OptionType.UNKNOWN, "An option requires an option type")

    val realOption = OptionData(op.type, op.name, op.description)
        .setRequired(op.required)
    for (choice in op.choices) {
        if (choice.second is Int) {
            realOption.addChoice(choice.first, choice.second as Int)
        } else if (choice.second is String) {
            realOption.addChoice(choice.first, choice.second.toString())
        }
    }
    return realOption
}

fun createSubCommand(cmd: KSubCommand.() -> Unit): SubcommandData {
    val subCommand = KSubCommand("", "")
    subCommand.cmd()
    Checks.check(subCommand.name.isNotBlank(), "A sub command requires a name")
    Checks.check(subCommand.description.isNotBlank(), "A sub command requires a description")
    return subCommand.subCommand
}

fun createSubCommandGroup(cmd: KSubCommandGroup.() -> Unit): SubcommandGroupData {
    val subCommandGroup = KSubCommandGroup("", "")
    subCommandGroup.cmd()
    Checks.check(subCommandGroup.name.isNotBlank(), "A sub command group requires a name")
    Checks.check(subCommandGroup.description.isNotBlank(), "A sub command group requires a description")
    return subCommandGroup.build()
}