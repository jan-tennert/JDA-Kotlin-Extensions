package de.jan.jdaktx.musichandler

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import java.time.Duration
import kotlin.math.abs

class AudioHandler {
    private val playerManager: AudioPlayerManager

    companion object {
        val musicManagers: HashMap<Long, GuildMusicManager> = HashMap()
    }

    @Synchronized
    fun getGuildAudioPlayer(guild: Guild): GuildMusicManager {
        val guildId = guild.id.toLong()
        var musicManager = musicManagers[guildId]
        if (musicManager == null) {
            musicManager = GuildMusicManager(playerManager, guild)
            musicManagers[guildId] = musicManager
        }
        guild.audioManager.sendingHandler = musicManager.sendHandler
        return musicManager
    }

    fun loadAndPlay(v: VoiceChannel, trackUrl: String) {
        val musicManager = getGuildAudioPlayer(v.guild)
        playerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                play(v, v.guild, musicManager, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                play(v, v.guild, musicManager, playlist.tracks)
            }

            override fun noMatches() {
            }

            override fun loadFailed(exception: FriendlyException) {
            }

            fun formatSeconds(timeInSeconds: Long): String {
                val dur = Duration.ofMillis(timeInSeconds)
                val absSeconds = abs(dur.seconds)
                val positive = String.format(
                    "%d:%02d:%02d",
                    absSeconds / 3600,
                    absSeconds % 3600 / 60,
                    absSeconds % 60
                )
                return if (absSeconds < 0) "-$positive" else positive
            }
        })
    }

    private fun play(
        v: VoiceChannel,
        guild: Guild,
        musicManager: GuildMusicManager,
        track: AudioTrack
    ) {
        guild.audioManager.openAudioConnection(v)
        musicManager.queue(track)
    }

    private fun play(
        v: VoiceChannel,
        guild: Guild,
        musicManager: GuildMusicManager,
        tracks: List<AudioTrack>
    ) {
        guild.audioManager.openAudioConnection(v)
        for (track in tracks) {
            musicManager.queue(track)
        }
    }

    fun getGuildMusicManager(channel: TextChannel): GuildMusicManager {
        return getGuildAudioPlayer(channel.guild)
    }

    fun skipTrack(channel: TextChannel) {
        val musicManager = getGuildAudioPlayer(channel.guild)
        musicManager.skip()
    }

    fun isPlayingIn(g: Guild): Boolean {
        return musicManagers[g.idLong] != null
    }

    init {
        playerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }
}

val Guild.musicManager: GuildMusicManager
    get() = AudioHandler().getGuildAudioPlayer(this)

fun VoiceChannel.play(url: String) {
    val audioHandler = AudioHandler()
    audioHandler.loadAndPlay(this, url)
}