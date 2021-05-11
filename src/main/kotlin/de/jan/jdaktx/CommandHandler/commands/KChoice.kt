package de.jan.jdaktx.CommandHandler.commands

class KChoice {

    val choices = mutableListOf<Pair<String, Any>>()

    operator fun Pair<String, Any>.unaryPlus() {
        choices.add(this)
    }

}