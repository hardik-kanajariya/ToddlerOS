package com.toddleros.ui.activities.piano

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.audio.PercussionType
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PianoKeyInfo(
    val noteName: String,
    val frequency: Float,
    val color: Color
)

@Composable
fun BabyPianoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val audioEngine = remember { ToddlerAudioEngine() }
    var isMuted by remember { mutableStateOf(false) }

    var isDemoPlaying by remember { mutableStateOf(false) }
    var activeKeyIndex by remember { mutableIntStateOf(-1) }
    var demoJob by remember { mutableStateOf<Job?>(null) }

    val keys = remember {
        listOf(
            PianoKeyInfo("Do", 261.63f, Color(0xFFEF4444)), // Red
            PianoKeyInfo("Re", 293.66f, Color(0xFFF97316)), // Orange
            PianoKeyInfo("Mi", 329.63f, Color(0xFFFBBF24)), // Amber
            PianoKeyInfo("Fa", 349.23f, Color(0xFF22C55E)), // Green
            PianoKeyInfo("Sol", 392.00f, Color(0xFF06B6D4)), // Cyan
            PianoKeyInfo("La", 440.00f, Color(0xFF3B82F6)), // Blue
            PianoKeyInfo("Ti", 493.88f, Color(0xFF8B5CF6)), // Purple
            PianoKeyInfo("Do", 523.25f, Color(0xFFEC4899))  // Pink
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            demoJob?.cancel()
            audioEngine.release()
        }
    }

    fun playKey(index: Int) {
        activeKeyIndex = index
        audioEngine.playPianoNote(keys[index].frequency)
    }

    fun toggleDemoSong() {
        if (isDemoPlaying) {
            demoJob?.cancel()
            isDemoPlaying = false
            activeKeyIndex = -1
        } else {
            isDemoPlaying = true
            demoJob = coroutineScope.launch {
                // Cheerful Nursery Melody (Twinkle Twinkle notes: 0,0, 4,4, 5,5, 4, 3,3, 2,2, 1,1, 0)
                val melody = listOf(0, 0, 4, 4, 5, 5, 4, 3, 3, 2, 2, 1, 1, 0)
                for (note in melody) {
                    if (!isDemoPlaying) break
                    activeKeyIndex = note
                    audioEngine.playPianoNote(keys[note].frequency, 450)
                    delay(500L)
                    activeKeyIndex = -1
                    delay(80L)
                }
                isDemoPlaying = false
                activeKeyIndex = -1
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Baby Piano 🎹",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
            }
        )

        // Top percussion drum pads & Demo button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Bass Kick Drum
                PercussionPad(
                    label = "🥁 Drum",
                    color = Color(0xFFFED7AA),
                    onClick = { audioEngine.playPercussion(PercussionType.KICK) }
                )
                // Clap
                PercussionPad(
                    label = "👏 Clap",
                    color = Color(0xFFBBF7D0),
                    onClick = { audioEngine.playPercussion(PercussionType.CLAP) }
                )
                // Shaker
                PercussionPad(
                    label = "🔔 Shaker",
                    color = Color(0xFFBAE6FD),
                    onClick = { audioEngine.playPercussion(PercussionType.SHAKER) }
                )
            }

            // Demo play/stop button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDemoPlaying) Color(0xFFF43F5E) else ToddlerPrimary)
                    .pointerInput(Unit) {
                        detectTapGestures { toggleDemoSong() }
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isDemoPlaying) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                        contentDescription = "Demo Melody",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = if (isDemoPlaying) "Stop" else "Song 🎵",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }

        // 8 Rainbow Piano Keys
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keys.forEachIndexed { index, key ->
                val isPressed = activeKeyIndex == index
                val scale = remember { Animatable(1f) }

                androidx.compose.runtime.LaunchedEffect(isPressed) {
                    if (isPressed) {
                        scale.animateTo(0.94f, tween(80))
                        scale.animateTo(1f, tween(120))
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .scale(scale.value)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isPressed) key.color.copy(alpha = 0.7f) else key.color)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    playKey(index)
                                }
                            )
                        }
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Key cap indicator circle
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ToddlerInk
                            )
                        }
                        Text(
                            text = key.noteName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PercussionPad(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color)
            .pointerInput(Unit) {
                detectTapGestures { onClick() }
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = ToddlerInk
        )
    }
}
