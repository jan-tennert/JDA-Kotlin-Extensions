package de.jan.jdaktx.musichandler.events

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import de.jan.jdaktx.musichandler.GuildMusicManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.Event

class AudioTrackEndEvent(
    val jda: JDA,
    val track: AudioTrack,
    val reason: AudioTrackEndReason,
    val musicManager: GuildMusicManager,
    val textChannel: TextChannel? = null
) : Event(jda)