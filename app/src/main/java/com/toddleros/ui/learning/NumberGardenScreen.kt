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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGardenScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var targetNumber by remember { mutableIntStateOf(3) }
    var currentCount by remember { mutableIntStateOf(0) }
    var completed by remember { mutableStateOf(false) }

    val fruits = remember { listOf("🍎", "🍓", "🍊", "🌻", "⭐") }
    var currentEmoji by remember { mutableStateOf(fruits[0]) }

    val tappedIndices = remember { mutableStateListOf<Boolean>() }

    fun resetRound(newTarget: Int) {
        targetNumber = newTarget
        currentCount = 0
        completed = false
        currentEmoji = fruits[(newTarget - 1) % fruits.size]
        tappedIndices.clear()
        repeat(newTarget) { tappedIndices.add(false) }
    }

    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                tts?.speak("Let's count together, Vasu! Tap each one!", TextToSpeech.QUEUE_FLUSH, null, "intro")
            }
        }
        textToSpeech = tts
        resetRound(3)

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Number Garden 🌻",
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
                IconButton(onClick = { resetRound((targetNumber % 7) + 1) }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "New Round",
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
            // Target counter banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(PastelMint)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (!completed) "Count $targetNumber items, Vasu!" else "Wonderful, Vasu! 🌟",
                        style = MaterialTheme.typography.titleLarge,
                        color = PastelMintDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Counted: $currentCount of $targetNumber",
                        style = MaterialTheme.typography.bodyLarge,
                        color = PastelMintDark,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Interactive counting items
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until targetNumber) {
                    val isTapped = tappedIndices.getOrNull(i) == true
                    Box(
                        modifier = Modifier
                            .size(if (targetNumber > 5) 54.dp else 68.dp)
                            .clip(CircleShape)
                            .background(if (isTapped) PastelYellow else ToddlerSurface)
                            .clickable {
                                if (!isTapped && !completed) {
                                    tappedIndices[i] = true
                                    currentCount++
                                    val countText = "$currentCount"
                                    textToSpeech?.speak(countText, TextToSpeech.QUEUE_FLUSH, null, "count_$currentCount")

                                    if (currentCount == targetNumber) {
                                        completed = true
                                        textToSpeech?.speak("Wonderful, Vasu! You counted $targetNumber!", TextToSpeech.QUEUE_ADD, null, "done")
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isTapped) "${i + 1}" else currentEmoji,
                            fontSize = if (isTapped) 28.sp else 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTapped) ToddlerInk else Color.Unspecified
                        )
                    }
                }
            }

            // Bottom action / celebration
            if (completed) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎉 Awesome job, Vasu! 🎉",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ToddlerPrimary,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { resetRound((targetNumber % 7) + 1) },
                        colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Next Adventure! 🚀", style = MaterialTheme.typography.labelLarge)
                    }
                }
            } else {
                Text(
                    text = "Tap each object to count it! 👆",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ToddlerInk
                )
            }
        }
    }
}
