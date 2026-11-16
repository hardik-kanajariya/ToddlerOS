package com.toddleros.ui.learning

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
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
import com.toddleros.core.theme.CategoryColors
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import java.util.Locale

data class AlphabetCard(val letter: Char, val word: String, val emoji: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetAdventureScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedIndex by remember { mutableIntStateOf(0) }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val alphabet = remember {
        listOf(
            AlphabetCard('A', "Apple", "🍎"),
            AlphabetCard('B', "Bear", "🐻"),
            AlphabetCard('C', "Cat", "🐱"),
            AlphabetCard('D', "Duck", "🦆"),
            AlphabetCard('E', "Elephant", "🐘"),
            AlphabetCard('F', "Fish", "🐟"),
            AlphabetCard('G', "Giraffe", "🦒"),
            AlphabetCard('H', "Horse", "🐴"),
            AlphabetCard('I', "Ice Cream", "🍦"),
            AlphabetCard('J', "Juice", "🧃"),
            AlphabetCard('K', "Kite", "🪁"),
            AlphabetCard('L', "Lion", "🦁"),
            AlphabetCard('M', "Monkey", "🐵"),
            AlphabetCard('N', "Nest", "🪺"),
            AlphabetCard('O', "Orange", "🍊"),
            AlphabetCard('P', "Penguin", "🐧"),
            AlphabetCard('Q', "Queen", "👑"),
            AlphabetCard('R', "Rainbow", "🌈"),
            AlphabetCard('S', "Sun", "☀️"),
            AlphabetCard('T', "Tiger", "🐯"),
            AlphabetCard('U', "Umbrella", "☂️"),
            AlphabetCard('V', "Violin", "🎻"),
            AlphabetCard('W', "Whale", "🐳"),
            AlphabetCard('X', "Xylophone", "🎶"),
            AlphabetCard('Y', "Yacht", "⛵"),
            AlphabetCard('Z', "Zebra", "🦓")
        )
    }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS ready
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speakItem(item: AlphabetCard) {
        val utterance = "${item.letter}! ${item.letter} is for ${item.word}!"
        textToSpeech?.speak(utterance, TextToSpeech.QUEUE_FLUSH, null, "letter_${item.letter}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Alphabet Adventure 🔤",
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
        )

        // Hero feature card for the selected letter
        val currentItem = alphabet[selectedIndex]
        val (cardBg, cardText) = CategoryColors[selectedIndex % CategoryColors.size]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(cardBg)
                .clickable { speakItem(currentItem) }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = currentItem.letter.toString(),
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = cardText
                    )
                    Text(
                        text = currentItem.emoji,
                        fontSize = 72.sp
                    )
                }
                Text(
                    text = "${currentItem.letter} is for ${currentItem.word}!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = cardText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = "Hear Sound",
                        tint = cardText,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Tap to listen!",
                        style = MaterialTheme.typography.labelLarge,
                        color = cardText
                    )
                }
            }
        }

        // 4-column alphabet grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(alphabet) { index, item ->
                val isSelected = index == selectedIndex
                val (itemBg, itemTextColor) = CategoryColors[index % CategoryColors.size]

                Box(
                    modifier = Modifier
                        .height(76.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ToddlerPrimary else itemBg)
                        .clickable {
                            selectedIndex = index
                            speakItem(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.letter.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else itemTextColor
                        )
                        Text(
                            text = item.emoji,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
