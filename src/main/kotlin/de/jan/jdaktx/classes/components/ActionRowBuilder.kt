package de.jan.jdaktx.classes.components

import de.jan.jdaktx.eventmanager.on
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.requests.restaction.MessageAction
import net.dv8tion.jda.internal.utils.Checks

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

    /**
     * Creates a blue button
     */
    fun primaryButton(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.PRIMARY, init)

    /**
     * Creates a grey button
     */
    fun secondaryButton(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.SECONDARY, init)

    /**
     * Creates a red button
     */
    fun dangerButton(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.DANGER, init)

    /**
     * Creates a green button
     */
    fun successButton(
        id: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KButton.() -> Unit
    ) = actionButton(id, label, emoji, disabled, ButtonStyle.SUCCESS, init)

    /**
     * Creates a link button
     */
    fun linkButton(
        url: String? = null,
        label: String? = null,
        emoji: Emoji? = null,
        disabled: Boolean = false,
        init: KLinkButton.() -> Unit
    ) {
        val button = KLinkButton(url, label, emoji, disabled)
        button.init()
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
        addSelectionMenuListener(menu)
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
        addButtonListener(button)
        components.add(button)
    }

    private fun addSelectionMenuListener(selectionMenu: KSelectionMenu) {
        if (selectionMenu.action != null) {
            jda?.on<SelectionMenuEvent>(predicate = { it.componentId == selectionMenu.id }) {
                selectionMenu.action!!.invoke(it)
            }
        }
    }

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

fun MessageAction.actionRowBuilder(init: ActionRowBuilder.() -> Unit): MessageAction {
    val builder = ActionRowBuilder(jda)
    builder.init()
    setActionRows(builder.rows)
    return this
}

fun MessageBuilder.actionRowBuilder(jda: JDA? = null, init: ActionRowBuilder.() -> Unit): MessageBuilder {
    val builder = ActionRowBuilder(jda)
    builder.init()
    setActionRows(builder.rows)
    return this
}