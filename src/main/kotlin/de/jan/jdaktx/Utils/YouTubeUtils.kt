package de.jan.jdaktx.Utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat

class YouTubeUtils(val apiKey: String) {

    private val okhttp = OkHttpClient()

    fun findVideos(query: String, maxResults: Int = 5, orderBy: Order = Order.RELEVANCE): List<Video> {
        val url =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=${maxResults}&order=relevance&q=${query}&type=video&order=${orderBy.key}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val result = JSONObject(okhttp.newCall(request).execute().body!!.string())
        val videos = mutableListOf<Video>()
        val items = result.getJSONArray("items")
        for (item in items) {
            val ob = item as JSONObject
            videos.add(Video(ob))
        }
        return videos.toList()
    }

    fun getVideoById(id: String): Video {
        val url =
            "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&id=${id}&key=${apiKey}"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val result = JSONObject(okhttp.newCall(request).execute().body!!.string())
        return Video(result.getJSONArray("items")[0] as JSONObject, id)
    }

    class Video(data: JSONObject, id: String? = null) {

        private val snippet = data.getJSONObject("snippet")
        val title = snippet.getString("title")
        val description = snippet.getString("description")
        val channelName = snippet.getString("channelTitle")
        val thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url")
        val channelID = snippet.getString("channelId")
        val publishedAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
            snippet.getString("publishedAt").replace("T", " ").replace("Z", "")
        )
        val videoID = id ?: data.getJSONObject("id").getString("videoId")

    }

    enum class Order(val key: String) {
        DATE("date"),
        RATING("rating"),
        RELEVANCE("relevance"),
        TITLE("title"),
        VIDEO_COUNT("videoCount"),
        VIEW_COUNT("viewCount")
    }

}