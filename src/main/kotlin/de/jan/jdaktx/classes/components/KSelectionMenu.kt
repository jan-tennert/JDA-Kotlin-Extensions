package de.jan.jdaktx.classes.components

import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu
import net.dv8tion.jda.internal.utils.Checks

class KSelectOptions {
    internal val options = mutableListOf<SelectOption>()

    fun option(
        label: String? = null,
        description: String? = null,
        value: String? = null,
        isDefault: Boolean = false,
        emoji: Emoji? = null,
        init: KSelectOption.() -> Unit = {}
    ) {
        val option = KSelectOption(label, description, value, isDefault, emoji)
        option.init()
        Checks.notNull(option.label, "SelectOption Label")
        Checks.notNull(option.value, "SelectOption Value")
        options.add(
            SelectOption.of(option.label!!, option.value!!)
                .withDescription(option.description)
                .withDefault(option.isDefault)
                .withEmoji(option.emoji)
        )
    }

    fun SelectOption.unaryPlus() {
        options.add(this)
    }

    fun KSelectOption.unaryPlus() {
        Checks.notNull(this.label, "SelectOption Label")
        Checks.notNull(this.value, "SelectOption Value")
        options.add(
            (SelectOption.of(this.label!!, this.value!!)
                .withDescription(this.description)
                .withDefault(this.isDefault)
                .withEmoji(this.emoji))
        )
    }
}

data class KSelectOption(
    var label: String? = null,
    var description: String? = null,
    var value: String? = null,
    var isDefault: Boolean = false,
    var emoji: Emoji? = null
)

class KSelectionMenu(
    var id: String? = null,
    var minValues: Int = 1,
    var maxValues: Int = 1,
    var placeHolder: String? = null
) : IComponent {

    private val options = mutableListOf<SelectOption>()
    internal var action: ((SelectionMenuEvent) -> Unit)? = null

    var range: Pair<Int, Int> = Pair(minValues, maxValues)
        get() {
            return Pair(minValues, maxValues)
        }
        set(value) {
            field = value
            minValues = value.first
            maxValues = value.second
        }

    fun options(init: KSelectOptions.() -> Unit) {
        val menu = KSelectOptions()
        menu.init()
        options.addAll(menu.options)
    }

    /**
     * Your lambda is run when the user has selected between [minValues] and [maxValues] options and closes the selection menu
     */
    fun action(init: (SelectionMenuEvent) -> Unit) {
        action = init
    }

    override fun toComponent(): Component {
        Checks.notNull(id, "SelectionMenu ID")
        return SelectionMenu.create(id!!)
            .setPlaceholder(placeHolder)
            .addOptions(options)
            .setRequiredRange(range.first, range.second)
            .build()
    }

    override fun toString(): String {
        return "KSelectionMenu(id=$id, minValues=$minValues, maxValues=$maxValues, placeHolder=$placeHolder)"
    }

}