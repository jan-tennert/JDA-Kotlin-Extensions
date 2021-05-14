package de.jan.jdaktx.commandhandler.commands

import net.dv8tion.jda.internal.utils.Checks

class KChoice {

    internal val choices = mutableListOf<Pair<String, Any>>()

    operator fun Pair<String, Any>.unaryPlus() {
        Checks.check(
            this.second is String && this.second is Int,
            "The key of a choice can only bee a string or an int"
        )
        choices.add(this)
    }

}