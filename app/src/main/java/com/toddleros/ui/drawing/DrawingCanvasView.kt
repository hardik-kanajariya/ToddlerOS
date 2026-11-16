package com.toddleros.ui.drawing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Dedicated low-latency Android View for smooth, continuous finger drawing.
 *
 * Utilizes a dual-buffer architecture (offscreen cached bitmap for completed strokes
 * + real-time hardware-accelerated active path), quadratic Bézier curve interpolation,
 * intermediate touch event processing, and zero-allocation hot paths.
 */
class DrawingCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentColor: Int = Color.parseColor("#E53935")
    private var currentStrokeWidth: Float = 16f

    private val strokes = ArrayList<DrawingStroke>()
    private val redoStrokes = ArrayList<DrawingStroke>()

    // Reusable active path & paint
    private val activePath = Path()
    private val activePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    // Reusable paint for rendering completed strokes onto cached bitmap
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    // Offscreen bitmap cache for completed strokes (O(1) active drawing performance)
    private var cachedBitmap: Bitmap? = null
    private var cachedCanvas: Canvas? = null

    // Touch tracking coordinates
    private var lastX = 0f
    private var lastY = 0f
    private var startX = 0f
    private var startY = 0f
    private var isCurrentStrokeDot = true

    private var controller: DrawingCanvasController? = null

    fun setController(ctrl: DrawingCanvasController) {
        this.controller = ctrl
        ctrl.attach(this)
    }

    fun setColor(color: Int) {
        if (this.currentColor != color) {
            this.currentColor = color
            activePaint.color = color
        }
    }

    fun setStrokeWidthPx(widthPx: Float) {
        if (this.currentStrokeWidth != widthPx) {
            this.currentStrokeWidth = widthPx
            activePaint.strokeWidth = widthPx
        }
    }

    fun canUndo(): Boolean = strokes.isNotEmpty()
    fun canRedo(): Boolean = redoStrokes.isNotEmpty()

    fun undo() {
        if (strokes.isNotEmpty()) {
            val removed = strokes.removeAt(strokes.size - 1)
            redoStrokes.add(removed)
            redrawCachedBitmap()
            invalidate()
            controller?.updateUndoRedoState()
        }
    }

    fun redo() {
        if (redoStrokes.isNotEmpty()) {
            val restored = redoStrokes.removeAt(redoStrokes.size - 1)
            strokes.add(restored)
            drawStrokeToCachedCanvas(restored)
            invalidate()
            controller?.updateUndoRedoState()
        }
    }

    fun clearCanvas() {
        strokes.clear()
        redoStrokes.clear()
        activePath.reset()
        cachedBitmap?.eraseColor(Color.TRANSPARENT)
        invalidate()
        controller?.updateUndoRedoState()
    }

    fun exportBitmap(targetWidth: Int, targetHeight: Int): Bitmap? {
        if (width <= 0 || height <= 0) return null

        val exportBmp = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val exportCanvas = Canvas(exportBmp)
        exportCanvas.drawColor(Color.WHITE)

        val scaleX = targetWidth.toFloat() / width.toFloat()
        val scaleY = targetHeight.toFloat() / height.toFloat()
        exportCanvas.scale(scaleX, scaleY)

        for (stroke in strokes) {
            strokePaint.color = stroke.color
            strokePaint.strokeWidth = stroke.strokeWidth
            exportCanvas.drawPath(stroke.path, strokePaint)
        }

        return exportBmp
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            if (cachedBitmap == null || cachedBitmap?.width != w || cachedBitmap?.height != h) {
                cachedBitmap?.recycle()
                cachedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                cachedCanvas = Canvas(cachedBitmap!!)
                redrawCachedBitmap()
            }
        }
    }

    private fun redrawCachedBitmap() {
        cachedBitmap?.eraseColor(Color.TRANSPARENT)
        val canvas = cachedCanvas ?: return
        for (stroke in strokes) {
            strokePaint.color = stroke.color
            strokePaint.strokeWidth = stroke.strokeWidth
            canvas.drawPath(stroke.path, strokePaint)
        }
    }

    private fun drawStrokeToCachedCanvas(stroke: DrawingStroke) {
        val canvas = cachedCanvas ?: return
        strokePaint.color = stroke.color
        strokePaint.strokeWidth = stroke.strokeWidth
        canvas.drawPath(stroke.path, strokePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                startX = event.x
                startY = event.y
                lastX = event.x
                lastY = event.y
                isCurrentStrokeDot = true

                activePath.reset()
                activePath.moveTo(event.x, event.y)
                activePaint.color = currentColor
                activePaint.strokeWidth = currentStrokeWidth

                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val historySize = event.historySize
                for (i in 0 until historySize) {
                    val hx = event.getHistoricalX(i)
                    val hy = event.getHistoricalY(i)
                    addMovePoint(hx, hy)
                }
                addMovePoint(event.x, event.y)

                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (isCurrentStrokeDot) {
                    // Draw a clean dot for taps without movement
                    activePath.lineTo(startX + 0.1f, startY + 0.1f)
                } else {
                    activePath.lineTo(event.x, event.y)
                }

                val completedStroke = DrawingStroke(
                    path = Path(activePath),
                    color = currentColor,
                    strokeWidth = currentStrokeWidth
                )
                strokes.add(completedStroke)
                redoStrokes.clear()

                drawStrokeToCachedCanvas(completedStroke)
                activePath.reset()

                controller?.updateUndoRedoState()
                invalidate()
                parent?.requestDisallowInterceptTouchEvent(false)
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                activePath.reset()
                invalidate()
                parent?.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun addMovePoint(x: Float, y: Float) {
        val dx = Math.abs(x - lastX)
        val dy = Math.abs(y - lastY)

        // Threshold to eliminate micro-jitter while keeping fast response
        if (dx >= 1f || dy >= 1f) {
            isCurrentStrokeDot = false
            // Quadratic Bézier midpoint interpolation for smooth, natural curves
            val midX = (lastX + x) / 2f
            val midY = (lastY + y) / 2f
            activePath.quadTo(lastX, lastY, midX, midY)
            lastX = x
            lastY = y
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Canvas paper surface
        canvas.drawColor(Color.WHITE)

        // 1. Draw cached completed strokes (O(1) hardware-accelerated bitmap blit)
        cachedBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // 2. Draw currently active smooth stroke
        if (!activePath.isEmpty) {
            canvas.drawPath(activePath, activePaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cachedBitmap?.recycle()
        cachedBitmap = null
        cachedCanvas = null
        controller?.detach()
    }
}
