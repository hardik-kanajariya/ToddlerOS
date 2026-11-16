package com.toddleros.ui.drawing

import android.graphics.Path

/**
 * Represents an immutable completed drawing stroke.
 */
data class DrawingStroke(
    val path: Path,
    val color: Int,
    val strokeWidth: Float
)
