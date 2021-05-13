package de.jan.jdaktx.classes

import net.dv8tion.jda.api.Permission

class Permissions {

    internal val allow = mutableListOf<Permission>()
    internal val deny = mutableListOf<Permission>()

    operator fun Permission.unaryPlus() {
        allow.add(this)
    }

    operator fun Permission.unaryMinus() {
        deny.add(this)
    }

}