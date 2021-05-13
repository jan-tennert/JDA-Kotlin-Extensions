package de.jan.jdaktx.classes.channel

import de.jan.jdaktx.classes.Permissions
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.ChannelAction

class KChannel<T : GuildChannel>(channelName: String, val channelAction: ChannelAction<T>) {

    var name: String = channelName
        set(value) {
            channelAction.setName(value)
            field = value
        }
    var parent: Category? = null
        set(value) {
            channelAction.setParent(value)
            field = value
        }
    var userLimit: Int? = null
        set(value) {
            channelAction.setUserlimit(value)
            field = value
        }
    var bitrate: Int? = null
        set(value) {
            channelAction.setBitrate(value)
            field = value
        }
    var nsfw: Boolean = false
        set(value) {
            channelAction.setNSFW(value)
            field = value
        }
    var news: Boolean = false
        set(value) {
            channelAction.setNews(value)
            field = value
        }
    var slowMode: Int? = null
        set(value) {
            channelAction.setSlowmode(value!!)
            field = value
        }
    var topic: String? = null
        set(value) {
            channelAction.setTopic(value)
            field = value
        }
    var position: Int? = null
        set(value) {
            channelAction.setPosition(value)
            field = value
        }

    fun clearPermissions() = channelAction.clearPermissionOverrides()

    fun syncPermissionsWithParent() = channelAction.syncPermissionOverrides()

    fun addMemberPermission(memberId: Long, permissions: Permissions.() -> Unit) {
        val perms = Permissions()
        perms.permissions()
        channelAction.addMemberPermissionOverride(memberId, perms.allow, perms.deny)
    }

    fun addMemberPermission(member: Member, permissions: Permissions.() -> Unit) =
        addMemberPermission(member.idLong, permissions)

    fun addPermission(memberId: Long, allow: Long, deny: Long) {
        channelAction.addMemberPermissionOverride(memberId, allow, deny)
    }

    fun addRolePermission(roleId: Long, permissions: Permissions.() -> Unit) {
        val perms = Permissions()
        perms.permissions()
        channelAction.addRolePermissionOverride(roleId, perms.allow, perms.deny)
    }

    fun addRolePermission(role: Role, permissions: Permissions.() -> Unit) = addRolePermission(role.idLong, permissions)

    fun addRolePermission(roleId: Long, allow: Long, deny: Long) {
        channelAction.addRolePermissionOverride(roleId, allow, deny)
    }

    fun addPermission(permissionHolder: IPermissionHolder, permissions: Permissions.() -> Unit) {
        val perms = Permissions()
        perms.permissions()
        channelAction.addPermissionOverride(permissionHolder, perms.allow, perms.deny)
    }

    fun addPermission(permissionHolder: IPermissionHolder, allow: Long, deny: Long) {
        channelAction.addPermissionOverride(permissionHolder, allow, deny)
    }

}

fun Guild.createTextChannel(name: String, init: KChannel<TextChannel>.() -> Unit): ChannelAction<TextChannel> {
    val textChannel = KChannel(name, this.createTextChannel(name))
    textChannel.init()
    return textChannel.channelAction
}

fun Guild.createVoiceChannel(name: String, init: KChannel<VoiceChannel>.() -> Unit): ChannelAction<VoiceChannel> {
    val voiceChannel = KChannel(name, this.createVoiceChannel(name))
    voiceChannel.init()
    return voiceChannel.channelAction
}

fun Guild.createCategory(name: String, init: KChannel<Category>.() -> Unit): ChannelAction<Category> {
    val category = KChannel(name, this.createCategory(name))
    category.init()
    return category.channelAction
}