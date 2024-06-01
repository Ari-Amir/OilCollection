package com.aco.oilcollection.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.alignBottomWithPadding(bottomPadding: Dp) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
        placeable.place(0, constraints.maxHeight - placeable.height - bottomPadding.roundToPx())
    }
}
