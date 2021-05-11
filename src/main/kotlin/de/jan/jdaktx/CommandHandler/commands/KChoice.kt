package de.jan.jdaktx.CommandHandler.commands

class KChoice {

    internal val choices = mutableListOf<Pair<String, Any>>()

    operator fun Pair<String, Any>.unaryPlus() {
        if (this.second !is String && this.second !is Int) throw IllegalArgumentException("The key of a choice can only bee a string or an int")
        choices.add(this)
    }

}