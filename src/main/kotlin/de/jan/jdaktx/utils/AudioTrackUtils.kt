package de.jan.jdaktx.utils

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlin.math.abs

/**
 * Returns the type of the track
 */
val AudioTrack.type: VideoType
    get() {
        val url = this.info.uri
        return if (url.isYouTubeURL) {
            VideoType.YOUTUBE
        } else if (url.contains("twitch.tv")) {
            VideoType.TWITCH
        } else if (url.contains("soundcloud.com")) {
            VideoType.SOUNDCLOUD
        } else {
            VideoType.UNKNOWN
        }
    }

/**
 * Formats the duration of the track to HH:mm:ss
 */
fun AudioTrack.toFormattedDuration(): String {
    val dur = java.time.Duration.ofMillis(this.duration)
    val absSeconds = abs(dur.seconds)
    val positive = String.format(
        "%d:%02d:%02d",
        absSeconds / 3600,
        absSeconds % 3600 / 60,
        absSeconds % 60
    )
    return if (absSeconds < 0) "-$positive" else positive
}

private val String.isYouTubeURL: Boolean
    get() {
        val pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+"
        return this.isNotBlank() && this.matches(Regex(pattern))
    }

enum class VideoType {
    YOUTUBE, TWITCH, SOUNDCLOUD, UNKNOWN
}