package com.toddleros.ui.activities.talking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class CharacterEmotion {
    HAPPY,
    LAUGHING,
    SURPRISED,
    CONFUSED
}

@Composable
fun TalkingCharacterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    var emotion by remember { mutableStateOf(CharacterEmotion.HAPPY) }
    var isListening by remember { mutableStateOf(false) }
    var isMouthOpen by remember { mutableStateOf(false) }
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    // Playful animated mouth flapping while Mimi is speaking
    fun animateSpeakingMouth(durationMs: Long) {
        coroutineScope.launch {
            val endTime = System.currentTimeMillis() + durationMs
            while (System.currentTimeMillis() < endTime) {
                isMouthOpen = !isMouthOpen
                delay(140L)
            }
            isMouthOpen = false
            emotion = CharacterEmotion.HAPPY
        }
    }

    fun speakWithEmotion(text: String, targetEmotion: CharacterEmotion, durationMs: Long = 1800L) {
        emotion = targetEmotion
        ttsManager.speakPlayful(text)
        animateSpeakingMouth(durationMs)
    }

    val playfulFallbackResponses = remember {
        listOf(
            "Hehehe! I love your voice, Vasu!",
            "Giggle giggle! You are so silly!",
            "Ooh! What a funny sound, Vasu!",
            "Yay! Peekaboo! I hear you!",
            "Achoo! Hehehe, that tickles!",
            "La la la! Let's sing together!"
        )
    }

    fun handleSpeechResult(spokenText: String?) {
        isListening = false
        if (!spokenText.isNullOrBlank()) {
            val repeatSpeech = "$spokenText! Giggle giggle!"
            speakWithEmotion(repeatSpeech, CharacterEmotion.LAUGHING, 2200L)
        } else {
            val fallback = playfulFallbackResponses.random()
            val chosenEmotion = listOf(
                CharacterEmotion.HAPPY,
                CharacterEmotion.LAUGHING,
                CharacterEmotion.SURPRISED
            ).random()
            speakWithEmotion(fallback, chosenEmotion, 2000L)
        }
    }

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            // Graceful fallback for devices without speech recognition service
            handleSpeechResult(null)
            return
        }

        try {
            speechRecognizer?.destroy()
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                        emotion = CharacterEmotion.SURPRISED
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        isListening = false
                    }

                    override fun onError(error: Int) {
                        isListening = false
                        // Never punish or show errors to toddler; respond playfully!
                        handleSpeechResult(null)
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull()
                        handleSpeechResult(text)
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
            speechRecognizer = recognizer

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            recognizer.startListening(intent)
        } catch (_: Exception) {
            handleSpeechResult(null)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startListening()
        } else {
            handleSpeechResult(null)
        }
    }

    fun onMicTapped() {
        audioEngine.playPianoNote(659.25f, 150)
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            startListening()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun onCharacterTapped() {
        // Tickling Mimi triggers playful giggle!
        audioEngine.playPianoNote(587.33f, 150)
        val giggles = listOf(
            "Hehehe! That tickles, Vasu!",
            "Giggle giggle! You touched my nose!",
            "Purr purr! Hello Vasu!"
        )
        speakWithEmotion(giggles.random(), CharacterEmotion.LAUGHING, 1600L)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer?.destroy()
            audioEngine.release()
            ttsManager.release()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Talking Mimi 🐱",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        // Character stage
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Interactive Talking Buddy "Mimi"
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(48.dp))
                        .background(PastelLavender)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onCharacterTapped()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    MimiCharacterCanvas(
                        emotion = emotion,
                        isMouthOpen = isMouthOpen
                    )
                }

                Text(
                    text = if (isListening) "Mimi is listening... 👂" else "Tap the mic to talk with Mimi! 🎤",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ToddlerInk,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Extra emotion trigger buttons for quick exploration
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EmotionPill(
                        label = "Giggle 😄",
                        color = PastelYellow,
                        onClick = {
                            speakWithEmotion("Hahaha! So funny!", CharacterEmotion.LAUGHING, 1500L)
                        }
                    )
                    EmotionPill(
                        label = "Surprise 😮",
                        color = PastelMint,
                        onClick = {
                            speakWithEmotion("Wow, look at that, Vasu!", CharacterEmotion.SURPRISED, 1500L)
                        }
                    )
                    EmotionPill(
                        label = "Silly 🤪",
                        color = PastelCoral,
                        onClick = {
                            speakWithEmotion("Bumble bee boop boop!", CharacterEmotion.CONFUSED, 1500L)
                        }
                    )
                }
            }
        }

        // Bottom Tap-to-Talk Big Microphone Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            val pulse = remember { Animatable(1f) }

            LaunchedEffect(isListening) {
                if (isListening) {
                    pulse.animateTo(
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(400, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                } else {
                    pulse.snapTo(1f)
                }
            }

            Box(
                modifier = Modifier
                    .size(86.dp)
                    .scale(pulse.value)
                    .clip(CircleShape)
                    .background(if (isListening) Color(0xFFEF4444) else ToddlerPrimary)
                    .clickable { onMicTapped() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Mic,
                    contentDescription = "Tap to talk",
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }
        }
    }
}

@Composable
private fun MimiCharacterCanvas(
    emotion: CharacterEmotion,
    isMouthOpen: Boolean
) {
    Canvas(modifier = Modifier.size(200.dp)) {
        val w = size.width
        val h = size.height
        val orange = Color(0xFFFB923C)
        val cream = Color(0xFFFEF3C7)
        val eyeColor = Color(0xFF1E293B)
        val pink = Color(0xFFF43F5E)

        // Triangular ears
        val leftEar = Path().apply {
            moveTo(w * 0.22f, h * 0.38f)
            lineTo(w * 0.25f, h * 0.12f)
            lineTo(w * 0.44f, h * 0.24f)
            close()
        }
        val rightEar = Path().apply {
            moveTo(w * 0.78f, h * 0.38f)
            lineTo(w * 0.75f, h * 0.12f)
            lineTo(w * 0.56f, h * 0.24f)
            close()
        }
        drawPath(leftEar, orange)
        drawPath(rightEar, orange)
        // Inner ears
        drawCircle(cream, radius = w * 0.05f, center = Offset(w * 0.3f, h * 0.24f))
        drawCircle(cream, radius = w * 0.05f, center = Offset(w * 0.7f, h * 0.24f))

        // Big round face
        drawCircle(orange, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.52f))

        // Cream muzzle
        drawOval(cream, topLeft = Offset(w * 0.33f, h * 0.5f), size = Size(w * 0.34f, h * 0.26f))

        // Cute pink nose
        drawOval(pink, topLeft = Offset(w * 0.46f, h * 0.54f), size = Size(w * 0.08f, h * 0.05f))

        // Blush cheeks
        drawCircle(Color(0xFFFCA5A5).copy(alpha = 0.7f), radius = w * 0.055f, center = Offset(w * 0.26f, h * 0.58f))
        drawCircle(Color(0xFFFCA5A5).copy(alpha = 0.7f), radius = w * 0.055f, center = Offset(w * 0.74f, h * 0.58f))

        // Eyes based on emotion
        when (emotion) {
            CharacterEmotion.HAPPY -> {
                drawCircle(eyeColor, radius = w * 0.045f, center = Offset(w * 0.38f, h * 0.46f))
                drawCircle(eyeColor, radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.46f))
                drawCircle(Color.White, radius = w * 0.016f, center = Offset(w * 0.39f, h * 0.45f))
                drawCircle(Color.White, radius = w * 0.016f, center = Offset(w * 0.63f, h * 0.45f))
            }
            CharacterEmotion.LAUGHING -> {
                // Happy curved arcs
                drawArc(
                    color = eyeColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.33f, h * 0.44f),
                    size = Size(w * 0.1f, h * 0.06f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
                drawArc(
                    color = eyeColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.57f, h * 0.44f),
                    size = Size(w * 0.1f, h * 0.06f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
            }
            CharacterEmotion.SURPRISED -> {
                // Wide open round eyes
                drawCircle(eyeColor, radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.45f))
                drawCircle(eyeColor, radius = w * 0.06f, center = Offset(w * 0.62f, h * 0.45f))
                drawCircle(Color.White, radius = w * 0.024f, center = Offset(w * 0.38f, h * 0.44f))
                drawCircle(Color.White, radius = w * 0.024f, center = Offset(w * 0.62f, h * 0.44f))
            }
            CharacterEmotion.CONFUSED -> {
                // One eye wide, one wink
                drawCircle(eyeColor, radius = w * 0.05f, center = Offset(w * 0.38f, h * 0.45f))
                drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.38f, h * 0.44f))
                drawArc(
                    color = eyeColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.57f, h * 0.45f),
                    size = Size(w * 0.1f, h * 0.05f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
            }
        }

        // Animated Mouth
        if (isMouthOpen) {
            // Open animated talking mouth
            drawOval(
                color = Color(0xFFDC2626),
                topLeft = Offset(w * 0.44f, h * 0.61f),
                size = Size(w * 0.12f, h * 0.09f)
            )
            drawOval(
                color = Color(0xFFFCA5A5),
                topLeft = Offset(w * 0.46f, h * 0.65f),
                size = Size(w * 0.08f, h * 0.04f)
            )
        } else {
            // Closed smiling mouth
            drawArc(
                color = Color(0xFF78350F),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.44f, h * 0.60f),
                size = Size(w * 0.12f, h * 0.06f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
            )
        }
    }
}

@Composable
private fun EmotionPill(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = ToddlerInk
        )
    }
}
