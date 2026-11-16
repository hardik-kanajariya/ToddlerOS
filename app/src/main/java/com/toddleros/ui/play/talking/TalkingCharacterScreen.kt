package com.toddleros.ui.play.talking

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.ToddlerBorder
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalkingCharacterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var expression by remember { mutableStateOf(CharacterExpression.IDLE) }
    var isSpeaking by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Say something or tap Mimi!") }
    var isListening by remember { mutableStateOf(false) }

    val voiceEngine = remember {
        TalkingVoiceEngine(
            context = context,
            onExpressionChange = { expression = it },
            onSpeakingStateChange = { isSpeaking = it },
            onStatusMessageChange = { statusMessage = it }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            voiceEngine.release()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isListening = true
            voiceEngine.startListening()
        } else {
            statusMessage = "Microphone permission is needed to listen!"
        }
    }

    fun toggleListening() {
        if (isListening) {
            isListening = false
            voiceEngine.stopListening()
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                isListening = true
                voiceEngine.startListening()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    val pulseTransition = rememberInfiniteTransition(label = "pulse_mic")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.14f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val micButtonColor by animateColorAsState(
        targetValue = if (isListening) Color(0xFFE53935) else ToddlerPrimary,
        label = "mic_color"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Talking Mimi 🐱",
                    style = MaterialTheme.typography.titleLarge,
                    color = ToddlerInk,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = ToddlerInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
        )

        // Speech Bubble / Status Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(ToddlerSurface)
                .border(1.dp, ToddlerBorder, RoundedCornerShape(24.dp))
                .padding(horizontal = 18.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = ToddlerInk,
                textAlign = TextAlign.Center
            )
        }

        // Animated Character Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            TalkingCharacterView(
                expression = expression,
                isSpeaking = isSpeaking,
                onTapArea = { area ->
                    voiceEngine.onCharacterTapped(area)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Playful Quick Reaction Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton(
                label = "Tickle! 🐾",
                onClick = { voiceEngine.onCharacterTapped(CharacterTapArea.BELLY) },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Sing! 🎵",
                onClick = {
                    voiceEngine.speakReaction(
                        "La la la! Tra la la! Happy music with Vasu!",
                        CharacterExpression.HAPPY
                    )
                },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Boop! 👃",
                onClick = { voiceEngine.onCharacterTapped(CharacterTapArea.NOSE) },
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom Controls: Large Accessible Microphone Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(micButtonColor)
                    .clickable { toggleListening() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = if (isListening) "Stop Listening" else "Start Listening",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Text(
                text = if (isListening) "Listening... Tap to stop" else "Tap Mic to Talk to Mimi",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isListening) Color(0xFFC62828) else ToddlerInkSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PastelPeach)
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = PastelPeachDark,
            textAlign = TextAlign.Center
        )
    }
}
