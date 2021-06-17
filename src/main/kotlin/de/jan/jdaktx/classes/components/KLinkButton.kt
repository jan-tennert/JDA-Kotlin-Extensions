package de.jan.jdaktx.classes.components

import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.internal.utils.Checks

class KLinkButton(
    var label: String? = null,
    var url: String? = null,
    var emoji: Emoji? = null,
    var disabled: Boolean = false
) : IComponent {
    override fun toComponent(): Button {
        Checks.notNull(url, "Button URL")
        if (label == null && emoji == null) throw IllegalArgumentException("You must specify a label or an emoji (or both)")
        var realButton: Button? = null
        if (label != null && emoji != null) realButton =
            Button.link(url!!, label!!).withEmoji(emoji!!).withDisabled(disabled)
        if (label != null && emoji == null) realButton = Button.link(url!!, label!!).withDisabled(disabled)
        if (label == null && emoji != null) realButton = Button.link(url!!, emoji!!).withDisabled(disabled)
        return realButton!!
    }

    override fun toString(): String {
        return "KLinkButton(label=$label, url=$url, emoji=$emoji, disabled=$disabled)"
    }

}