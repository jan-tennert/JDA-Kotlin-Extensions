package de.jan.jdaktx.classes.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.internal.utils.Checks
import java.awt.Color

class KEmbedBuilder {

    private val embedBuilder = EmbedBuilder()
    var footer: String? = null
        set(value) {
            field = value
            embedBuilder.setFooter(value, null)
        }
    var title: String? = null
        set(value) {
            field = value
            embedBuilder.setTitle(value, null)
        }
    var author: String? = null
        set(value) {
            field = value
            embedBuilder.setTitle(value, null)
        }
    var thumbnail: String? = null
        set(value) {
            field = value
            embedBuilder.setThumbnail(value)
        }
    var image: String? = null
        set(value) {
            field = value
            embedBuilder.setImage(value)
        }
    var description: String? = null
        set(value) {
            field = value
            embedBuilder.setDescription(value)
        }
    var color: Color? = null
        set(value) {
            field = value
            embedBuilder.setColor(value)
        }

    fun title(init: Title.() -> Unit) {
        val title = Title()
        title.init()
        embedBuilder.setTitle(title.title, title.url)
    }

    fun author(init: Author.() -> Unit) {
        val author = Author()
        author.init()
        embedBuilder.setAuthor(author.name, author.url, author.iconURL)
    }

    fun footer(c: Footer.() -> Unit) {
        val footer = Footer()
        footer.c()
        embedBuilder.setFooter(footer.text, footer.iconURL)
    }

    fun field(init: Field.() -> Unit) {
        val field = Field()
        field.init()
        Checks.check(!field.text.isNullOrBlank(), "An embed field name can't be empty")
        Checks.check(!field.value.isNullOrBlank(), "An embed field value can't be empty")
        embedBuilder.addField(field.text, field.value, field.inline)
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

data class Footer(var text: String? = null, var iconURL: String? = null)
data class Title(var title: String? = null, var url: String? = null)
data class Author(var name: String? = null, var url: String? = null, var iconURL: String? = null)
data class Field(var text: String? = null, var value: String? = null, var inline: Boolean = false)

fun messageEmbed(init: KEmbedBuilder.() -> Unit): MessageEmbed {
    val builder = KEmbedBuilder()
    builder.init()
    return builder.build()
}