package de.jan.jdaktx.commandhandler

import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData

fun List<net.dv8tion.jda.api.interactions.commands.Command.Subcommand>.toSubcommandData(): List<SubcommandData> {
    val data = mutableListOf<SubcommandData>()
    forEach {
        data.add(SubcommandData(it.name, it.description).addOptions(it.options.toOptionData()))
    }
    return data
}

fun List<net.dv8tion.jda.api.interactions.commands.Command.Option>.toOptionData(): List<OptionData> {
    val data = mutableListOf<OptionData>()
    forEach {
        data.add(OptionData(it.type, it.name, it.description, false).addChoices(it.choices))
    }
    return data
}

fun Command.isTheSameAs(other: net.dv8tion.jda.api.interactions.commands.Command): Boolean {

    fun List<net.dv8tion.jda.api.interactions.commands.Command.Option>.isTheSameAs(commands: List<OptionData>): Boolean {
        if (size != commands.size) return false
        val sameBasic = map {
            it.name to it.type to it.description to it.choices
        } == commands.map {
            it.name to it.type to it.description to it.choices
        }
        return sameBasic
    }

    fun List<net.dv8tion.jda.api.interactions.commands.Command.Subcommand>.isTheSameAs(data: List<SubcommandData>): Boolean {
        if (size != data.size) return false
        val sameBasic = map {
            it.name to it.description
        } == data.map {
            it.name to it.description
        }
        return sameBasic
    }

    fun List<net.dv8tion.jda.api.interactions.commands.Command.SubcommandGroup>.isTheSameAs(data: List<SubcommandGroupData>): Boolean {
        if (size != data.size) return false
        val sameBasic = map {
            it.name to it.description
        } == data.map {
            it.name to it.description
        }
        return sameBasic
    }

    return other.name == name && other.description == description && other.options.isTheSameAs(options.toList()) && other.subcommands.isTheSameAs(
        subcommands
    ) && other.subcommandGroups.isTheSameAs(subcommandGroups)
}