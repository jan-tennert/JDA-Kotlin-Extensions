package de.jan.jdaktx.classes.components

import de.jan.jdaktx.eventmanager.on
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.internal.utils.Checks

class ActionRowBuilder(private val jda: JDA? = null) {

    internal val rows = mutableListOf<ActionRow>()

    fun row(init: KActionRow.() -> Unit) {
        val row = KActionRow(jda)
        row.init()
        rows.add(ActionRow.of(row.buttons.map { it.toButton() }))
    }

}

class KActionRow(private val jda: JDA?) {

    internal val buttons = mutableListOf<IButton>()

    fun primary(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.PRIMARY, init)

    fun secondary(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.SECONDARY, init)

    fun danger(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.DANGER, init)

    fun success(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.SUCCESS, init)

    fun link(
        url: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KLinkButton.() -> Unit
    ) {
        val button = KLinkButton(url, label, emoji)
        button.init()
        if (url != null) button.url = url
        if (label != null) button.label = label
        if (emoji != null) button.emoji = emoji
        if (disabled != button.disabled) button.disabled = disabled
        buttons.add(button)
    }

    private fun actionButton(
        id: String?,
        label: String?,
        emoji: Emoji?,
        disabled: Boolean,
        style: ButtonStyle,
        init: KButton.() -> Unit
    ) {
        val button = KButton(id, label, emoji, disabled, style)
        button.init()
        if (id != null) button.id = id
        if (label != null) button.label = label
        if (emoji != null) button.emoji = emoji
        if (disabled != button.disabled) button.disabled = disabled
        addButtonListener(button)
        buttons.add(button)
    }

    private fun addButtonListener(button: KButton) {
        if (button.action != null) {
            jda?.on<ButtonClickEvent>(predicate = { it.componentId == button.id }) {
                button.action!!.invoke(it)
            }
        }
    }

}

interface IButton {
    fun toButton(): Button
}

class KLinkButton(
    var label: String? = null,
    var url: String? = null,
    var emoji: Emoji? = null,
    var disabled: Boolean = false
) : IButton {
    override fun toButton(): Button {
        Checks.notNull(url, "Button URL")
        if (label == null && emoji == null) throw IllegalArgumentException("You must specify a label or an emoji (or both)")
        var realButton: Button? = null
        if (label != null && emoji != null) realButton =
            Button.link(url!!, label!!).withEmoji(emoji!!).withDisabled(disabled)
        if (label != null && emoji == null) realButton = Button.link(url!!, label!!).withDisabled(disabled)
        if (label == null && emoji != null) realButton = Button.link(url!!, emoji!!).withDisabled(disabled)
        return realButton!!
    }
}

class KButton(
    var id: String? = null,
    var label: String? = null,
    var emoji: Emoji? = null,
    var disabled: Boolean = false,
    private val style: ButtonStyle
) : IButton {
    internal var action: ((ButtonClickEvent) -> Unit)? = null

    fun action(action: (ButtonClickEvent) -> Unit) {
        this.action = action
    }

    override fun toButton(): Button {
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

fun actionRowBuilder(jda: JDA? = null, init: ActionRowBuilder.() -> Unit): List<ActionRow> {
    val row = ActionRowBuilder(jda)
    row.init()
    return row.rows.toList()
}