package com.toddleros.ui.activities.bubbles

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

data class Bubble(
    val id: Long,
    var x: Float,
    var y: Float,
    val radius: Float,
    val speed: Float,
    val wobbleFreq: Float,
    val wobblePhase: Float,
    val color: Color,
    val isStar: Boolean
)

data class PopParticle(
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    var alpha: Float = 1f
)

@Composable
fun BubblePopScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val audioEngine = remember { ToddlerAudioEngine() }
    var isMuted by remember { mutableStateOf(false) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val bubbles = remember { mutableStateListOf<Bubble>() }
    val particles = remember { mutableStateListOf<PopParticle>() }
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
        }
    }

    val bubbleColors = remember {
        listOf(
            Color(0xFF93C5FD), // Sky blue
            Color(0xFFF9A8D4), // Soft Pink
            Color(0xFF86EFAC), // Mint Green
            Color(0xFFFDE047), // Butter Yellow
            Color(0xFFC4B5FD), // Lavender
            Color(0xFFFDBA74)  // Peach
        )
    }

    fun spawnBubble(startY: Float? = null): Bubble {
        val width = if (containerSize.width > 0) containerSize.width.toFloat() else 800f
        val height = if (containerSize.height > 0) containerSize.height.toFloat() else 1400f
        val radius = Random.nextFloat() * 28f + 42f // 42dp - 70dp large radius for easy touch
        val isStar = Random.nextFloat() < 0.22f // 22% chance of special star bubble

        return Bubble(
            id = System.nanoTime() + Random.nextLong(),
            x = Random.nextFloat() * (width - radius * 2) + radius,
            y = startY ?: (height + radius + Random.nextFloat() * 100f),
            radius = radius,
            speed = Random.nextFloat() * 60f + 70f,
            wobbleFreq = Random.nextFloat() * 2f + 1f,
            wobblePhase = Random.nextFloat() * 6.28f,
            color = bubbleColors.random(),
            isStar = isStar
        )
    }

    fun popBubble(bubble: Bubble) {
        audioEngine.playBubblePop()

        // Create burst particles
        val count = if (bubble.isStar) 12 else 8
        for (i in 0 until count) {
            val angle = (6.283f / count) * i
            val speed = Random.nextFloat() * 140f + 60f
            particles.add(
                PopParticle(
                    x = bubble.x,
                    y = bubble.y,
                    vx = kotlin.math.cos(angle) * speed,
                    vy = kotlin.math.sin(angle) * speed,
                    color = if (bubble.isStar) Color(0xFFFACC15) else bubble.color
                )
            )
        }

        bubbles.remove(bubble)
        bubbles.add(spawnBubble())
    }

    // Animation loop
    LaunchedEffect(containerSize) {
        if (containerSize.width == 0 || containerSize.height == 0) return@LaunchedEffect

        if (bubbles.isEmpty()) {
            val height = containerSize.height.toFloat()
            for (i in 0 until 10) {
                bubbles.add(spawnBubble(startY = height * (i / 10f)))
            }
        }

        var lastFrame = System.nanoTime()
        while (true) {
            withFrameNanos { now ->
                val dt = ((now - lastFrame) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastFrame = now
                timeSeconds += dt

                // Update bubbles
                val height = containerSize.height.toFloat()
                for (i in bubbles.indices) {
                    val b = bubbles[i]
                    b.y -= b.speed * dt
                    b.x += sin(timeSeconds * b.wobbleFreq + b.wobblePhase) * 22f * dt

                    // If floats off top, recycle to bottom
                    if (b.y < -b.radius * 2) {
                        bubbles[i] = spawnBubble()
                    }
                }

                // Update particles
                val iterator = particles.iterator()
                while (iterator.hasNext()) {
                    val p = iterator.next()
                    p.x += p.vx * dt
                    p.y += p.vy * dt
                    p.alpha -= 2.2f * dt
                    if (p.alpha <= 0f) {
                        iterator.remove()
                    }
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
            title = "Bubble Pop 🫧",
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
                        // Generous hit slop: radius + 40px padding for forgiving 18-month-old taps
                        val hit = bubbles.lastOrNull { b ->
                            val dist = hypot(b.x - tapOffset.x, b.y - tapOffset.y)
                            dist <= (b.radius + 40f)
                        }
                        if (hit != null) {
                            popBubble(hit)
                        } else {
                            // If tapped empty space, play soft bubble pop to always reward touch
                            audioEngine.playBubblePop()
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw bubbles
                for (b in bubbles) {
                    drawBubble(b)
                }

                // Draw pop burst particles
                for (p in particles) {
                    drawCircle(
                        color = p.color.copy(alpha = p.alpha.coerceIn(0f, 1f)),
                        radius = 8f * p.alpha,
                        center = Offset(p.x, p.y)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBubble(b: Bubble) {
    val center = Offset(b.x, b.y)

    // Outer translucent body
    drawCircle(
        color = b.color.copy(alpha = 0.45f),
        radius = b.radius,
        center = center
    )

    // Crisp soft border
    drawCircle(
        color = b.color.copy(alpha = 0.85f),
        radius = b.radius,
        center = center,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f)
    )

    // Top-left glossy highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.75f),
        radius = b.radius * 0.22f,
        center = Offset(b.x - b.radius * 0.35f, b.y - b.radius * 0.35f)
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.5f),
        radius = b.radius * 0.1f,
        center = Offset(b.x - b.radius * 0.15f, b.y - b.radius * 0.48f)
    )

    // Star icon inside special bubble
    if (b.isStar) {
        val starSize = b.radius * 0.7f
        drawCircle(
            color = Color(0xFFFDE047),
            radius = starSize * 0.5f,
            center = center
        )
        drawCircle(
            color = Color(0xFFEA580C),
            radius = starSize * 0.22f,
            center = center
        )
    }
}
