package de.jan.jdaktx.commandhandler.commands

import net.dv8tion.jda.api.interactions.commands.OptionType

class KOption(var name: String, var description: String, var type: OptionType, var required: Boolean) {

    internal val choices = mutableListOf<Pair<String, Any>>()

    fun addChoice(choice: String, value: String) = choices.add(choice to value)

    fun addChoice(choice: String, value: Int) = choices.add(choice to value)

    fun choices(choices: KChoice.() -> Unit) {
        val choice = KChoice()
        this.choices.addAll(choice.choices)
    }

}
