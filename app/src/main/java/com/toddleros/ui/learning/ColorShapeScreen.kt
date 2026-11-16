package com.toddleros.ui.learning

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.launch
import java.util.Locale

data class ShapeOption(val name: String, val colorName: String, val color: Color, val shapeSymbol: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorShapeScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val options = remember {
        listOf(
            ShapeOption("Red Circle", "Red", Color(0xFFEF5350), "🔴"),
            ShapeOption("Blue Square", "Blue", Color(0xFF42A5F5), "🟦"),
            ShapeOption("Yellow Star", "Yellow", Color(0xFFFFCA28), "⭐"),
            ShapeOption("Green Triangle", "Green", Color(0xFF66BB6A), "🔺"),
            ShapeOption("Purple Diamond", "Purple", Color(0xFFAB47BC), "🔷"),
            ShapeOption("Orange Heart", "Orange", Color(0xFFFFA726), "🧡")
        )
    }

    var targetIndex by remember { mutableIntStateOf(0) }
    var solved by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    fun speakQuest(opt: ShapeOption) {
        val quest = "Vasu, can you find the ${opt.name}?"
        textToSpeech?.speak(quest, TextToSpeech.QUEUE_FLUSH, null, "quest")
    }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                speakQuest(options[targetIndex])
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val currentTarget = options[targetIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Colors & Shapes 🎨",
                    style = MaterialTheme.typography.titleLarge,
                    color = ToddlerInk
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
            actions = {
                IconButton(onClick = {
                    targetIndex = (targetIndex + 1) % options.size
                    solved = false
                    feedbackMessage = ""
                    speakQuest(options[targetIndex])
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Shuffle",
                        tint = ToddlerInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Quest Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(ToddlerSurface)
                    .clickable { speakQuest(currentTarget) }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (solved) "🎉 Wonderful, Vasu! 🎉" else "Find the...",
                        style = MaterialTheme.typography.titleMedium,
                        color = ToddlerInk
                    )
                    Text(
                        text = currentTarget.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = currentTarget.color,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Tap to hear quest 🔊",
                        style = MaterialTheme.typography.bodySmall,
                        color = ToddlerInk.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            // Grid of choices
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                items(options) { opt ->
                    val isMatch = opt.name == currentTarget.name
                    Box(
                        modifier = Modifier
                            .height(110.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(opt.color.copy(alpha = 0.2f))
                            .clickable {
                                if (isMatch) {
                                    solved = true
                                    feedbackMessage = "You found the ${opt.name}! Great job, Vasu!"
                                    textToSpeech?.speak("You found it! Wonderful, Vasu!", TextToSpeech.QUEUE_FLUSH, null, "win")
                                    coroutineScope.launch {
                                        progressRepository.recordCompletion("color_shapes", 2)
                                    }
                                } else {
                                    feedbackMessage = "That's ${opt.name}! Let's find ${currentTarget.name}!"
                                    textToSpeech?.speak(feedbackMessage, TextToSpeech.QUEUE_FLUSH, null, "hint")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = opt.shapeSymbol,
                                fontSize = 44.sp
                            )
                            Text(
                                text = opt.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ToddlerInk,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            if (solved) {
                Button(
                    onClick = {
                        targetIndex = (targetIndex + 1) % options.size
                        solved = false
                        feedbackMessage = ""
                        speakQuest(options[targetIndex])
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Next Shape! 🌟", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Text(
                    text = feedbackMessage.ifBlank { "Tap the matching shape for Vasu!" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = ToddlerInk,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
