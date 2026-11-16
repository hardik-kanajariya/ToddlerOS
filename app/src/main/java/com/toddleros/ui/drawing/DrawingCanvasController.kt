package com.toddleros.ui.drawing

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Controller to bridge Jetpack Compose UI actions (undo, redo, clear, export)
 * with the underlying high-performance DrawingCanvasView.
 */
class DrawingCanvasController {

    private var canvasView: DrawingCanvasView? = null

    private val _canUndo = mutableStateOf(false)
    val canUndo: State<Boolean> = _canUndo

    private val _canRedo = mutableStateOf(false)
    val canRedo: State<Boolean> = _canRedo

    internal fun attach(view: DrawingCanvasView) {
        this.canvasView = view
        updateUndoRedoState()
    }

    internal fun detach() {
        this.canvasView = null
    }

    internal fun updateUndoRedoState() {
        _canUndo.value = canvasView?.canUndo() == true
        _canRedo.value = canvasView?.canRedo() == true
    }

    fun undo() {
        canvasView?.undo()
        updateUndoRedoState()
    }

    fun redo() {
        canvasView?.redo()
        updateUndoRedoState()
    }

    fun clear() {
        canvasView?.clearCanvas()
        updateUndoRedoState()
    }

    fun exportBitmap(targetWidth: Int = 800, targetHeight: Int = 800): Bitmap? {
        return canvasView?.exportBitmap(targetWidth, targetHeight)
    }
}
