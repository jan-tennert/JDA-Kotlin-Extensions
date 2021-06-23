package de.jan.jdaktx.musichandler.events

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import de.jan.jdaktx.musichandler.GuildMusicManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.Event

class AudioTrackQueueEvent(
    val jda: JDA,
    val track: AudioTrack,
    val musicManager: GuildMusicManager,
    val textChannel: TextChannel? = null
) : Event(jda)