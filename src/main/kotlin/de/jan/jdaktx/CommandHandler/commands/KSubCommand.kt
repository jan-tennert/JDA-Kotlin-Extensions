package de.jan.jdaktx.CommandHandler.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

class KSubCommand(name: String, description: String) {

    val subCommand = SubcommandData(name, description)
    var name: String = ""
        set(value) {
            field = value
            subCommand.name = value
        }
    var description: String = ""
        set(value) {
            field = value
            subCommand.description = value
        }

    fun option(
        name: String = "",
        description: String = "",
        type: OptionType = OptionType.UNKNOWN,
        required: Boolean = false,
        option: KOption.() -> Unit
    ) {
        val o = KOption(name, description, type, required)
        o.option()
        if (o.name.isBlank()) throw IllegalArgumentException("An option requires a name")
        if (o.description.isBlank()) throw IllegalArgumentException("An option requires a description")
        if (o.type == OptionType.UNKNOWN) throw IllegalArgumentException("An option requires a option type")
        val realOption = OptionData(o.type, o.name, o.description)
            .setRequired(o.required)
        for (choice in o.choices) {
            if (choice.second is Int) {
                realOption.addChoice(choice.first, choice.second as Int)
            } else if (choice.second is String) {
                realOption.addChoice(choice.first, choice.second.toString())
            }
        }
        subCommand.addOption(realOption)
    }

}