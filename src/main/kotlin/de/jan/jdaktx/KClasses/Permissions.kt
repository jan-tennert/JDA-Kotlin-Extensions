package de.jan.jdaktx.KClasses

import net.dv8tion.jda.api.Permission

class Permissions {

    val allow = mutableListOf<Permission>()
    val deny = mutableListOf<Permission>()

    operator fun Permission.unaryPlus() {
        allow.add(this)
    }

    operator fun Permission.unaryMinus() {
        deny.add(this)
    }

}