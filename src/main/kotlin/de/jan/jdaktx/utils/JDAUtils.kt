package de.jan.jdaktx.utils

import java.awt.Color

operator fun Color.component1(): Int {
    return red
}

operator fun Color.component2(): Int {
    return green
}

operator fun Color.component3(): Int {
    return blue
}

operator fun Color.component4(): Int {
    return alpha
}