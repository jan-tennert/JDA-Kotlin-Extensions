package de.jan.jdaktx.commandhandler.commands

class KChoice {

    internal val choices = mutableListOf<Pair<String, Any>>()

    infix fun String.value(value: String) {
        choices.add(Pair(this, value))
    }

    infix fun String.value(value: Int) {
        choices.add(Pair(this, value))
    }

}