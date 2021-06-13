package de.jan.jdaktx.classes.components

import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.internal.utils.Checks

class KButton(
    var id: String? = null,
    var label: String? = null,
    var emoji: Emoji? = null,
    var disabled: Boolean = false,
    private val style: ButtonStyle
) : IComponent {
    internal var action: ((ButtonClickEvent) -> Unit)? = null

    fun action(action: (ButtonClickEvent) -> Unit) {
        this.action = action
    }

    override fun toComponent(): Button {
        Checks.notNull(id, "Button ID")
        if (label == null && emoji == null) throw IllegalArgumentException("You must specify a label or an emoji (or both)")
        var realButton: Button? = null
        if (label != null && emoji != null) realButton =
            Button.of(style, id!!, label!!).withEmoji(emoji!!).withDisabled(disabled)
        if (label != null && emoji == null) realButton = Button.of(style, id!!, label!!).withDisabled(disabled)
        if (label == null && emoji != null) realButton = Button.of(style, id!!, emoji!!).withDisabled(disabled)
        return realButton!!
    }
}