package de.jan.jdaktx.KClasses.KRoles

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.restaction.RoleAction
import java.awt.Color

class KRole(guild: Guild) {

    val roleAction: RoleAction = guild.createRole()
    var name: String? = null
        set(value) {
            field = value
            roleAction.setName(value)
        }
    var color: Color? = null
        set(value) {
            field = value
            roleAction.setColor(color)
        }
    var mentionable: Boolean = false
        set(value) {
            field = value
            roleAction.setMentionable(field)
        }
    var hoisted: Boolean = false
        set(value) {
            field = value
            roleAction.setHoisted(field)
        }

    fun setPermissions(vararg permissions: Permission) {
        roleAction.setPermissions(permissions.toList())
    }

    fun setPermissions(permissions: Long?) {
        roleAction.setPermissions(permissions)
    }

    fun setPermissions(permissions: Collection<Permission>) {
        roleAction.setPermissions(permissions)
    }
}

fun Guild.createRole(init: KRole.() -> Unit): RoleAction {
    val role = KRole(this)
    role.init()
    return role.roleAction
}


