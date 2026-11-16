package com.toddleros.ui.activities.balloons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

data class Balloon(
    val id: Long,
    var x: Float,
    var y: Float,
    val width: Float,
    val height: Float,
    var speed: Float,
    val wobblePhase: Float,
    val color: Color,
    var isFloatingAway: Boolean = false
)

data class BalloonSparkle(
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    var alpha: Float = 1f
)

@Composable
fun MagicBalloonsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val audioEngine = remember { ToddlerAudioEngine() }
    var isMuted by remember { mutableStateOf(false) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val balloons = remember { mutableStateListOf<Balloon>() }
    val sparkles = remember { mutableStateListOf<BalloonSparkle>() }
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    val balloonPalette = remember {
        listOf(
            Color(0xFFF87171), // Coral Red
            Color(0xFF60A5FA), // Sky Blue
            Color(0xFF4ADE80), // Fresh Mint
            Color(0xFFFBBF24), // Golden Amber
            Color(0xFFA78BFA), // Lavender
            Color(0xFFFB923C), // Cheerful Orange
            Color(0xFFF472B6)  // Pretty Pink
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
        }
    }

    fun spawnBalloon(startY: Float? = null): Balloon {
        val w = if (containerSize.width > 0) containerSize.width.toFloat() else 800f
        val h = if (containerSize.height > 0) containerSize.height.toFloat() else 1400f
        val bWidth = Random.nextFloat() * 24f + 70f // 70-94px width (generous for toddlers)
        val bHeight = bWidth * 1.25f

        return Balloon(
            id = System.nanoTime() + Random.nextLong(),
            x = Random.nextFloat() * (w - bWidth * 2) + bWidth,
            y = startY ?: (h + bHeight + Random.nextFloat() * 120f),
            width = bWidth,
            height = bHeight,
            speed = Random.nextFloat() * 45f + 55f,
            wobblePhase = Random.nextFloat() * 6.28f,
            color = balloonPalette.random()
        )
    }

    fun handleBalloonTouch(b: Balloon) {
        if (Random.nextBoolean()) {
            // Pop balloon
            audioEngine.playBalloonPop()
            for (i in 0 until 10) {
                val angle = (6.283f / 10) * i
                val speed = Random.nextFloat() * 160f + 60f
                sparkles.add(
                    BalloonSparkle(
                        x = b.x,
                        y = b.y,
                        vx = kotlin.math.cos(angle) * speed,
                        vy = kotlin.math.sin(angle) * speed,
                        color = b.color
                    )
                )
            }
            balloons.remove(b)
            balloons.add(spawnBalloon())
        } else {
            // Rapidly float away with whoosh sound!
            audioEngine.playPianoNote(659.25f, 180)
            b.isFloatingAway = true
            b.speed = 360f
        }
    }

    // Animation loop
    LaunchedEffect(containerSize) {
        if (containerSize.width == 0 || containerSize.height == 0) return@LaunchedEffect

        if (balloons.isEmpty()) {
            val h = containerSize.height.toFloat()
            for (i in 0 until 8) {
                balloons.add(spawnBalloon(startY = h * (i / 8f)))
            }
        }

        var lastFrame = System.nanoTime()
        while (true) {
            withFrameNanos { now ->
                val dt = ((now - lastFrame) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastFrame = now
                timeSeconds += dt

                for (i in balloons.indices) {
                    val b = balloons[i]
                    b.y -= b.speed * dt
                    b.x += sin(timeSeconds * 1.8f + b.wobblePhase) * 16f * dt

                    if (b.y < -b.height * 2) {
                        balloons[i] = spawnBalloon()
                    }
                }

                val it = sparkles.iterator()
                while (it.hasNext()) {
                    val s = it.next()
                    s.x += s.vx * dt
                    s.y += s.vy * dt
                    s.alpha -= 2.0f * dt
                    if (s.alpha <= 0f) it.remove()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Magic Balloons 🎈",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .onSizeChanged { containerSize = it }
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val hit = balloons.lastOrNull { b ->
                            val dist = hypot(b.x - tapOffset.x, b.y - tapOffset.y)
                            dist <= (b.width * 0.7f + 40f)
                        }
                        if (hit != null) {
                            handleBalloonTouch(hit)
                        } else {
                            audioEngine.playBalloonPop()
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Balloons
                for (b in balloons) {
                    drawBalloonItem(b)
                }

                // Sparkles
                for (s in sparkles) {
                    drawCircle(
                        color = s.color.copy(alpha = s.alpha.coerceIn(0f, 1f)),
                        radius = 8f * s.alpha,
                        center = Offset(s.x, s.y)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBalloonItem(b: Balloon) {
    val top = b.y - b.height / 2
    val left = b.x - b.width / 2

    // Balloon body (oval)
    drawOval(
        color = b.color,
        topLeft = Offset(left, top),
        size = Size(b.width, b.height)
    )

    // Highlight
    drawOval(
        color = Color.White.copy(alpha = 0.5f),
        topLeft = Offset(left + b.width * 0.18f, top + b.height * 0.14f),
        size = Size(b.width * 0.25f, b.height * 0.35f)
    )

    // Knot at base
    drawCircle(
        color = b.color,
        radius = b.width * 0.1f,
        center = Offset(b.x, top + b.height + 4f)
    )

    // Wavy string hanging down
    val stringPath = Path().apply {
        moveTo(b.x, top + b.height + 6f)
        cubicTo(
            b.x - 12f, top + b.height + 25f,
            b.x + 12f, top + b.height + 45f,
            b.x - 6f, top + b.height + 70f
        )
    }
    drawPath(
        path = stringPath,
        color = Color(0xFF64748B),
        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
    )
}
