package de.jan.jdaktx.classes.components

import de.jan.jdaktx.eventmanager.on
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.api.interactions.components.Component

class ActionRowBuilder(private val jda: JDA? = null) {

    internal val rows = mutableListOf<ActionRow>()

    fun row(init: KActionRow.() -> Unit) {
        val row = KActionRow(jda)
        row.init()
        rows.add(ActionRow.of(row.components.map { it.toComponent() }))
    }

}

class KActionRow(private val jda: JDA?) {

    internal val components = mutableListOf<IComponent>()

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
        components.add(button)
    }

    fun selectionMenu(
        id: String? = null,
        minValues: Int = 1,
        maxValues: Int = 1,
        placeHolder: String? = null,
        init: KSelectionMenu.() -> Unit
    ) {
        val menu = KSelectionMenu(id, minValues, maxValues, placeHolder)
        menu.init()
        if (id != null) menu.id = id
        if (placeHolder != null) menu.placeHolder = placeHolder
        if (minValues != 1) menu.minValues = minValues
        if (maxValues != 1) menu.maxValues = maxValues
        components.add(menu)
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
        components.add(button)
    }

    private fun addSelectionMenuListener(): Nothing = TODO()

    private fun addButtonListener(button: KButton) {
        if (button.action != null) {
            jda?.on<ButtonClickEvent>(predicate = { it.componentId == button.id }) {
                button.action!!.invoke(it)
            }
        }
    }

}

interface IComponent {
    fun toComponent(): Component
}


fun actionRowBuilder(jda: JDA? = null, init: ActionRowBuilder.() -> Unit): List<ActionRow> {
    val row = ActionRowBuilder(jda)
    row.init()
    return row.rows.toList()
}
