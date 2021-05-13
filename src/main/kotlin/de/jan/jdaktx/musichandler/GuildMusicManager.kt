package de.jan.jdaktx.musichandler

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import java.util.concurrent.LinkedBlockingQueue

class GuildMusicManager(manager: AudioPlayerManager, val guild: Guild) {

    val player: AudioPlayer = manager.createPlayer()
    private val BASS_BOOST = floatArrayOf(
        0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,
        -0.1f, -0.1f, -0.1f, -0.1f
    )

    val scheduler = TrackScheduler(player, guild)
    var volume: Int = 100
        set(value) {
            field = value
            player.volume = value
        }

    val sendHandler: AudioPlayerSendHandler
        get() = AudioPlayerSendHandler(player)
    var tracks: List<AudioTrack> = scheduler.queue.toList()
        get() = scheduler.queue.toList()
        private set
    val playingTrack: AudioTrack?
        get() = player.playingTrack
    var bassboost: Boolean = false
        set(value) {
            field = value
            if (value) {
                for (i in BASS_BOOST.indices) {
                    scheduler.equalizer.setGain(i, BASS_BOOST[i] + (1000))
                }
            } else {
                scheduler.resetEqualizer()
            }
        }
    var loop: Boolean = false
    var loopQueue: Boolean = false
    val position: Long
        get() {
            if (playingTrack != null) return playingTrack!!.position
            return 0L
        }
    val endsIn: Long
        get() {
            if (playingTrack != null) return playingTrack!!.duration - playingTrack!!.position
            return 0L
        }

    fun pause() {
        player.isPaused = true
    }

    fun resume() {
        player.isPaused = false
    }

    fun queue(track: AudioTrack) {
        scheduler.queue(track)
    }

    fun skip() {
        scheduler.nextTrack(playingTrack!!)
    }

    fun stop() {
        player.destroy()
        scheduler.queue.clear()
        guild.audioManager.closeAudioConnection()
    }

    fun shuffleQueue() {
        val shuffled = tracks.shuffled()
        val queue = LinkedBlockingQueue<AudioTrack>()
        queue.addAll(shuffled)
        tracks = shuffled
        scheduler.queue = queue
    }

    fun forwards(milliseconds: Long) {
        playingTrack!!.position = playingTrack!!.position + milliseconds
    }

    fun backwards(milliseconds: Long) {
        playingTrack!!.position = playingTrack!!.position - milliseconds
    }

    fun setPosition(milliseconds: Long) {
        playingTrack?.position = milliseconds
    }

    init {
        player.addListener(scheduler)
    }
}