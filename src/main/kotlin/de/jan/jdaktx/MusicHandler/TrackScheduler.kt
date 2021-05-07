package de.jan.jdaktx.MusicHandler

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.Guild
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(private val player: AudioPlayer, private val guild: Guild) : AudioEventAdapter() {
    val equalizer = EqualizerFactory()
    var queue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()
    private val reset = FloatArray(15)

    init {
        player.setFilterFactory(equalizer)
    }

    fun queue(track: AudioTrack) {
        if (player.startTrack(track, true)) {
        } else {
            queue.offer(track)
        }
    }

    fun nextTrack(loopTrack: AudioTrack) {
        if (guild.musicManager.loop) {
            player.startTrack(loopTrack.makeClone(), false)
        } else if (guild.musicManager.loopQueue) {
            queue.offer(loopTrack)
            val nextTrack = queue.poll()
            if (nextTrack != null) {
                val t = nextTrack.makeClone()
                player.startTrack(t, false)
            } else {
                guild.musicManager.stop()
            }
        } else {
            val nextTrack = queue.poll()
            if (nextTrack != null) {
                val t = nextTrack.makeClone()
                player.startTrack(t, false)
            } else {
              //  guild.musicManager.stop()
            }
        }
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) {
            nextTrack(track)
        }
    }

    fun resetEqualizer() {
        for (i in reset.indices) {
            equalizer.setGain(i, reset[i])
        }
    }

}