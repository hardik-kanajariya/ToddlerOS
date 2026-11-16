package com.toddleros.ui.activities.dancing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toddleros.core.audio.PercussionType
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

enum class DanceStyle {
    BOUNCE_SWAY,
    WIGGLE_SPIN,
    STAR_JUMP
}

@Composable
fun DancingCharactersScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val audioEngine = remember { ToddlerAudioEngine() }
    var isMuted by remember { mutableStateOf(false) }

    var bearDance by remember { mutableStateOf(DanceStyle.BOUNCE_SWAY) }
    var bunnyDance by remember { mutableStateOf(DanceStyle.WIGGLE_SPIN) }
    var pandaDance by remember { mutableStateOf(DanceStyle.STAR_JUMP) }

    // Rhythmic beat loop
    LaunchedEffect(isMuted) {
        var beat = 0
        while (isActive) {
            if (!isMuted) {
                if (beat % 2 == 0) {
                    audioEngine.playPercussion(PercussionType.KICK)
                } else {
                    audioEngine.playPercussion(PercussionType.SHAKER)
                }
            }
            beat++
            delay(480L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
        }
    }

    fun cycleDance(current: DanceStyle): DanceStyle {
        audioEngine.playPianoNote(523.25f, 200)
        return when (current) {
            DanceStyle.BOUNCE_SWAY -> DanceStyle.WIGGLE_SPIN
            DanceStyle.WIGGLE_SPIN -> DanceStyle.STAR_JUMP
            DanceStyle.STAR_JUMP -> DanceStyle.BOUNCE_SWAY
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Dancing Friends 💃",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
            }
        )

        Text(
            text = "Tap a friend to change their dance move!",
            style = MaterialTheme.typography.titleMedium,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        // Stage dance floor
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(PastelSky.copy(alpha = 0.35f)),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Stage platform
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFFEF08A))
            )

            // Characters on the dance floor
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Bear
                DancingCharacterItem(
                    name = "Bear",
                    color = Color(0xFFB45309),
                    danceStyle = bearDance,
                    onTap = { bearDance = cycleDance(bearDance) },
                    drawCharacter = { w, h -> drawBearCharacter(w, h) }
                )

                // Bunny
                DancingCharacterItem(
                    name = "Bunny",
                    color = Color(0xFFF472B6),
                    danceStyle = bunnyDance,
                    onTap = { bunnyDance = cycleDance(bunnyDance) },
                    drawCharacter = { w, h -> drawBunnyCharacter(w, h) }
                )

                // Panda
                DancingCharacterItem(
                    name = "Panda",
                    color = Color(0xFF1E293B),
                    danceStyle = pandaDance,
                    onTap = { pandaDance = cycleDance(pandaDance) },
                    drawCharacter = { w, h -> drawPandaCharacter(w, h) }
                )
            }
        }
    }
}

@Composable
private fun DancingCharacterItem(
    name: String,
    color: Color,
    danceStyle: DanceStyle,
    onTap: () -> Unit,
    drawCharacter: androidx.compose.ui.graphics.drawscope.DrawScope.(Float, Float) -> Unit
) {
    val bounceY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(danceStyle) {
        when (danceStyle) {
            DanceStyle.BOUNCE_SWAY -> {
                rotation.snapTo(0f)
                scale.snapTo(1f)
                bounceY.animateTo(
                    targetValue = -30f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(280, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            DanceStyle.WIGGLE_SPIN -> {
                bounceY.snapTo(0f)
                scale.snapTo(1f)
                rotation.animateTo(
                    targetValue = 18f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(220, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            DanceStyle.STAR_JUMP -> {
                rotation.snapTo(0f)
                bounceY.animateTo(
                    targetValue = -45f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(320, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                scale.animateTo(
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(320, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = bounceY.value.dp)
            .rotate(rotation.value)
            .scale(scale.value)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onTap()
            }
    ) {
        Canvas(modifier = Modifier.size(110.dp)) {
            drawCharacter(size.width, size.height)
        }
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ToddlerInk,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBearCharacter(w: Float, h: Float) {
    val brown = Color(0xFF92400E)
    val inner = Color(0xFFFDE68A)
    // Ears
    drawCircle(brown, radius = w * 0.16f, center = Offset(w * 0.22f, h * 0.25f))
    drawCircle(inner, radius = w * 0.09f, center = Offset(w * 0.22f, h * 0.25f))
    drawCircle(brown, radius = w * 0.16f, center = Offset(w * 0.78f, h * 0.25f))
    drawCircle(inner, radius = w * 0.09f, center = Offset(w * 0.78f, h * 0.25f))
    // Body & Head
    drawCircle(brown, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.52f))
    // Snout
    drawOval(inner, topLeft = Offset(w * 0.36f, h * 0.5f), size = Size(w * 0.28f, h * 0.24f))
    drawCircle(Color(0xFF451A03), radius = w * 0.045f, center = Offset(w * 0.5f, h * 0.56f))
    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.4f, h * 0.44f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.6f, h * 0.44f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.41f, h * 0.43f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.61f, h * 0.43f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBunnyCharacter(w: Float, h: Float) {
    val white = Color.White
    val pink = Color(0xFFF472B6)
    // Long ears
    drawOval(white, topLeft = Offset(w * 0.24f, h * 0.04f), size = Size(w * 0.18f, h * 0.48f))
    drawOval(pink, topLeft = Offset(w * 0.28f, h * 0.1f), size = Size(w * 0.1f, h * 0.36f))
    drawOval(white, topLeft = Offset(w * 0.58f, h * 0.04f), size = Size(w * 0.18f, h * 0.48f))
    drawOval(pink, topLeft = Offset(w * 0.62f, h * 0.1f), size = Size(w * 0.1f, h * 0.36f))
    // Head
    drawCircle(white, radius = w * 0.34f, center = Offset(w * 0.5f, h * 0.56f))
    // Cheeks
    drawCircle(pink.copy(alpha = 0.4f), radius = w * 0.06f, center = Offset(w * 0.32f, h * 0.62f))
    drawCircle(pink.copy(alpha = 0.4f), radius = w * 0.06f, center = Offset(w * 0.68f, h * 0.62f))
    // Nose
    drawCircle(pink, radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.6f))
    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.4f, h * 0.52f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.6f, h * 0.52f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.41f, h * 0.51f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.61f, h * 0.51f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPandaCharacter(w: Float, h: Float) {
    val black = Color(0xFF1E293B)
    val white = Color.White
    // Black ears
    drawCircle(black, radius = w * 0.15f, center = Offset(w * 0.22f, h * 0.26f))
    drawCircle(black, radius = w * 0.15f, center = Offset(w * 0.78f, h * 0.26f))
    // White head
    drawCircle(white, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.52f))
    // Eye patches
    drawOval(black, topLeft = Offset(w * 0.32f, h * 0.44f), size = Size(w * 0.16f, h * 0.14f))
    drawOval(black, topLeft = Offset(w * 0.52f, h * 0.44f), size = Size(w * 0.16f, h * 0.14f))
    // Eyes
    drawCircle(Color.White, radius = w * 0.03f, center = Offset(w * 0.4f, h * 0.5f))
    drawCircle(Color.White, radius = w * 0.03f, center = Offset(w * 0.6f, h * 0.5f))
    drawCircle(black, radius = w * 0.015f, center = Offset(w * 0.4f, h * 0.5f))
    drawCircle(black, radius = w * 0.015f, center = Offset(w * 0.6f, h * 0.5f))
    // Nose
    drawOval(black, topLeft = Offset(w * 0.45f, h * 0.6f), size = Size(w * 0.1f, h * 0.06f))
}
