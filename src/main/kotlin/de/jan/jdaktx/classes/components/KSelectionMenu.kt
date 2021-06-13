package de.jan.jdaktx.classes.components

import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu
import net.dv8tion.jda.internal.utils.Checks

class KSelectOptions {
    internal val options = mutableListOf<SelectOption>()

    fun option(init: KSelectOption.() -> Unit) {
        val option = KSelectOption()
        option.init()
        Checks.notNull(option.label, "SelectOption Label")
        Checks.notNull(option.value, "SelectOption Value")
        options.add(SelectOption(option.label!!, option.value!!, option.description, option.isDefault, option.emoji))
    }

    fun SelectOption.plus() {
        options.add(this)
    }

    fun KSelectOption.plus() {
        Checks.notNull(this.label, "SelectOption Label")
        Checks.notNull(this.value, "SelectOption Value")
        options.add(SelectOption(this.label!!, this.value!!, this.description, this.isDefault, this.emoji))
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
    var range: Pair<Int, Int> = Pair(minValues, maxValues)
        get() {
            return Pair(minValues, maxValues)
        }
        set(value) {
            field = value
            minValues = value.first
            minValues = value.second
        }

    fun options(init: KSelectOptions.() -> Unit) {
        val menu = KSelectOptions()
        menu.init()
        options.addAll(menu.options)
    }

    override fun toComponent(): Component {
        Checks.notNull(id, "SelectionMenu ID")
        return SelectionMenu.Builder(id!!)
            .setPlaceholder(placeHolder)
            .addOptions(options)
            .setRequiredRange(range.first, range.second)
            .build()
    }
}