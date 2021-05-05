package de.jan.jdaktx.CommandHandler

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.CommandHook
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class Command(
    name: String,
    description: String,
    var guildID: Long? = null,
    val permissions: Array<Permission> = arrayOf(),
    val botPermissions: Array<Permission> = arrayOf()
) : CommandData(name, description) {

    abstract fun run(
        channel: TextChannel?,
        member: Member?,
        user: User,
        privateChannel: PrivateChannel?,
        hook: CommandHook,
        options: MutableList<SlashCommandEvent.OptionData>,
        event: SlashCommandEvent
    )


}