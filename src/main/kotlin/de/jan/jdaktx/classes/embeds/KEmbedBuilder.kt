package de.jan.jdaktx.classes.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.internal.utils.Checks
import java.awt.Color
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAccessor
import java.util.*

class KEmbedBuilder {

    private val embedBuilder = EmbedBuilder()

    fun title(title: String?, url: String? = null) {
        embedBuilder.setTitle(title, url)
    }

    fun author(name: String?, url: String? = null, iconURL: String? = null) {
        embedBuilder.setAuthor(name, url, iconURL)
    }

    fun color(color: Int) {
        embedBuilder.setColor(color)
    }

    fun color(color: Color?) {
        embedBuilder.setColor(color)
    }

    fun image(imageURL: String?) {
        embedBuilder.setImage(imageURL)
    }

    fun thumbnail(url: String?) {
        embedBuilder.setThumbnail(url)
        embedBuilder.addField(MessageEmbed.Field("", "", true))
    }

    fun footer(text: String?, iconURL: String? = null) {
        embedBuilder.setFooter(text, iconURL)
    }

    fun timestamp(timestamp: TemporalAccessor) {
        embedBuilder.setTimestamp(timestamp)
    }

    fun timestamp(timestamp: Date) {
        embedBuilder.setTimestamp(timestamp.toInstant())
    }

    fun timestamp(timestamp: LocalDate) {
        embedBuilder.setTimestamp(timestamp.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    fun description(description: String?) {
        embedBuilder.setDescription(description)
    }

    fun appendDescription(description: String) {
        embedBuilder.appendDescription(description)
    }

    fun field(init: KEmbedField.() -> Unit) {
        val field = KEmbedField()
        field.init()
        Checks.check(field.name.isNotBlank(), "An embed field name can't be empty")
        Checks.check(field.value.isNotBlank(), "An embed field value can't be empty")
        embedBuilder.addField(field.name, field.value, field.inline)
    }

    fun field(name: String, value: String, inline: Boolean) {
        embedBuilder.addField(name, value, inline)
    }

    fun blankField(inline: Boolean = false) {
        embedBuilder.addBlankField(inline)
    }

    fun clearFields() {
        embedBuilder.clearFields()
    }

    fun clear() {
        embedBuilder.clear()
    }

    fun build(): MessageEmbed {
        return embedBuilder.build()
    }

}

fun messageEmbed(init: KEmbedBuilder.() -> Unit): MessageEmbed {
    val builder = KEmbedBuilder()
    builder.init()
    return builder.build()
}