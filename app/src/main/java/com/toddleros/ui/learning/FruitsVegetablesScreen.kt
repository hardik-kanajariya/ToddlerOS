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
import androidx.compose.material.icons.rounded.VolumeUp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.CategoryColors
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.launch
import java.util.Locale

data class ProduceItem(val name: String, val emoji: String, val funFact: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitsVegetablesScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val items = remember {
        listOf(
            ProduceItem("Apple", "🍎", "Sweet, crunchy red apple!"),
            ProduceItem("Banana", "🍌", "Yummy yellow banana!"),
            ProduceItem("Strawberry", "🍓", "Juicy red strawberry!"),
            ProduceItem("Carrot", "🥕", "Crunchy orange carrot!"),
            ProduceItem("Broccoli", "🥦", "Healthy little green tree!"),
            ProduceItem("Corn", "🌽", "Golden sweet corn on the cob!"),
            ProduceItem("Watermelon", "🍉", "Cool and refreshing watermelon!"),
            ProduceItem("Grapes", "🍇", "Little purple sweet grapes!"),
            ProduceItem("Orange", "🍊", "Bright sunny citrus orange!")
        )
    }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Ready
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speakProduce(item: ProduceItem) {
        val speech = "${item.name}! ${item.funFact}"
        textToSpeech?.speak(speech, TextToSpeech.QUEUE_FLUSH, null, "produce_${item.name}")
        coroutineScope.launch {
            progressRepository.recordCompletion("fruits_veggies", 1)
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
                    text = "Fruits & Veggies 🍎",
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

        // Hero card for selected produce
        val currentItem = items[selectedIndex]
        val (cardBg, cardText) = CategoryColors[selectedIndex % CategoryColors.size]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(cardBg)
                .clickable { speakProduce(currentItem) }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentItem.emoji, fontSize = 72.sp)
                Text(
                    text = currentItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = cardText,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = currentItem.funFact,
                    style = MaterialTheme.typography.titleMedium,
                    color = cardText,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Pronounce",
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

        // Produce grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                val isSelected = index == selectedIndex
                val (itemBg, itemTextColor) = CategoryColors[index % CategoryColors.size]

                Box(
                    modifier = Modifier
                        .height(96.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isSelected) ToddlerPrimary else itemBg)
                        .clickable {
                            selectedIndex = index
                            speakProduce(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = item.emoji, fontSize = 32.sp)
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else itemTextColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
