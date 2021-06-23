package de.jan.jdaktx.musichandler

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import de.jan.jdaktx.musichandler.events.AudioTrackEndEvent
import de.jan.jdaktx.musichandler.events.AudioTrackPlayEvent
import de.jan.jdaktx.musichandler.events.AudioTrackQueueEvent
import de.jan.jdaktx.utils.fireEvent
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
        if (!player.startTrack(track, true)) {
            guild.jda.fireEvent(
                AudioTrackQueueEvent(
                    guild.jda,
                    track,
                    guild.musicManager,
                    textChannel = guild.musicManager.queueTextChannel
                )
            )
            queue.offer(track)
        } else {
            guild.jda.fireEvent(
                AudioTrackPlayEvent(
                    guild.jda,
                    track,
                    guild.musicManager,
                    channel = guild.musicManager.currentlyPlayingIn,
                    textChannel = guild.musicManager.queueTextChannel
                )
            )
        }
    }

    fun nextTrack(loopTrack: AudioTrack) {
        if (guild.musicManager.loop) {
            player.startTrack(loopTrack.makeClone(), false)
            guild.jda.fireEvent(
                AudioTrackPlayEvent(
                    guild.jda,
                    loopTrack,
                    guild.musicManager,
                    true,
                    channel = guild.musicManager.currentlyPlayingIn,
                    textChannel = guild.musicManager.queueTextChannel
                )
            )
        } else if (guild.musicManager.loopQueue) {
            queue.offer(loopTrack)
            val nextTrack = queue.poll()
            if (nextTrack != null) {
                val t = nextTrack.makeClone()
                player.startTrack(t, false)
                guild.jda.fireEvent(
                    AudioTrackPlayEvent(
                        guild.jda,
                        loopTrack,
                        guild.musicManager,
                        loopQueue = true,
                        channel = guild.musicManager.currentlyPlayingIn,
                        textChannel = guild.musicManager.queueTextChannel
                    )
                )
            } else {
                guild.musicManager.stop()
            }
        } else {
            val nextTrack = queue.poll()
            if (nextTrack != null) {
                val t = nextTrack.makeClone()
                player.startTrack(t, false)
                guild.jda.fireEvent(
                    AudioTrackPlayEvent(
                        guild.jda,
                        loopTrack,
                        guild.musicManager,
                        channel = guild.musicManager.currentlyPlayingIn,
                        textChannel = guild.musicManager.queueTextChannel
                    )
                )
            } else {
                guild.musicManager.stop()
            }
        }
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        guild.jda.fireEvent(
            AudioTrackEndEvent(
                guild.jda,
                track,
                endReason,
                guild.musicManager,
                textChannel = guild.musicManager.queueTextChannel
            )
        )
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