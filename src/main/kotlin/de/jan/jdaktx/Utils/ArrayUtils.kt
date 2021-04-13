package de.jan.jdaktx.Utils

fun <T>Collection<T>.toFormattedString(separator: String = ", ") : String {
    var string = ""
    for ((index, t) in this.withIndex()) {
        if(index != this.size - 1) {
            string += t.toString() + separator
        } else {
            string += t.toString()
        }
    }
    return string
}

fun <T>Array<T>.toFormattedString(separator: String = ", ") : String {
    var string = ""
    for ((index, t) in this.withIndex()) {
        if(index != this.size - 1) {
            string += t.toString() + separator
        } else {
            string += t.toString()
        }
    }
    return string
}