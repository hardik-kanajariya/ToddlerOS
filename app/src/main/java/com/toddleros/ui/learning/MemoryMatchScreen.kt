package com.toddleros.ui.learning

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

data class MemoryCard(val id: Int, val emoji: String, var isFlipped: Boolean = false, var isMatched: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryMatchScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val emojis = remember { listOf("🐶", "🐱", "🦁", "🐼") }
    val cards = remember { mutableStateListOf<MemoryCard>() }
    var firstFlippedIndex by remember { mutableIntStateOf(-1) }
    var isChecking by remember { mutableStateOf(false) }
    var allMatched by remember { mutableStateOf(false) }

    fun setupGame() {
        val pairs = (emojis + emojis).shuffled()
        cards.clear()
        pairs.forEachIndexed { index, em ->
            cards.add(MemoryCard(id = index, emoji = em))
        }
        firstFlippedIndex = -1
        isChecking = false
        allMatched = false
    }

    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                tts?.speak("Let's match the friendly animals, Vasu!", TextToSpeech.QUEUE_FLUSH, null, "intro")
            }
        }
        textToSpeech = tts
        setupGame()

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun onCardClick(index: Int) {
        if (isChecking || cards[index].isFlipped || cards[index].isMatched) return

        cards[index] = cards[index].copy(isFlipped = true)

        if (firstFlippedIndex == -1) {
            firstFlippedIndex = index
        } else {
            val prevIndex = firstFlippedIndex
            val card1 = cards[prevIndex]
            val card2 = cards[index]

            if (card1.emoji == card2.emoji) {
                cards[prevIndex] = cards[prevIndex].copy(isMatched = true)
                cards[index] = cards[index].copy(isMatched = true)
                firstFlippedIndex = -1
                textToSpeech?.speak("Match! Great job, Vasu!", TextToSpeech.QUEUE_FLUSH, null, "match")

                if (cards.all { it.isMatched }) {
                    allMatched = true
                    textToSpeech?.speak("You matched all the animals, Vasu! Outstanding!", TextToSpeech.QUEUE_ADD, null, "win")
                }
            } else {
                isChecking = true
                coroutineScope.launch {
                    delay(1000L)
                    cards[prevIndex] = cards[prevIndex].copy(isFlipped = false)
                    cards[index] = cards[index].copy(isFlipped = false)
                    firstFlippedIndex = -1
                    isChecking = false
                }
            }
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
                    text = "Memory Matching 🧩",
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
                IconButton(onClick = { setupGame() }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Reset",
                        tint = ToddlerInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(cards) { index, card ->
                    val showContent = card.isFlipped || card.isMatched
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (showContent) PastelYellow else PastelCoral)
                            .clickable { onCardClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (showContent) {
                            Text(text = card.emoji, fontSize = 52.sp)
                        } else {
                            Text(text = "⭐", fontSize = 40.sp)
                        }
                    }
                }
            }

            if (allMatched) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "🎉 You Won, Vasu! Amazing Memory! 🎉",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ToddlerPrimary,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { setupGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Play Again! 🚀", style = MaterialTheme.typography.labelLarge)
                    }
                }
            } else {
                Text(
                    text = "Tap two cards to find animal pairs! 🐾",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ToddlerInk,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}
