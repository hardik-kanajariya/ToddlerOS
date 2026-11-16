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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
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

data class AnimalInfo(val name: String, val emoji: String, val sound: String, val habitat: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalWorldScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val animals = remember {
        listOf(
            AnimalInfo("Lion", "🦁", "Roar!", "Savannah"),
            AnimalInfo("Elephant", "🐘", "Pawoo!", "Jungle"),
            AnimalInfo("Monkey", "🐵", "Ooh ooh aah aah!", "Treetops"),
            AnimalInfo("Puppy", "🐶", "Woof woof!", "Home"),
            AnimalInfo("Kitten", "🐱", "Meow meow!", "Cozy Room"),
            AnimalInfo("Duck", "🦆", "Quack quack!", "Pond"),
            AnimalInfo("Frog", "🐸", "Ribbit ribbit!", "Rainforest"),
            AnimalInfo("Sheep", "🐑", "Baa baa!", "Green Meadow"),
            AnimalInfo("Cow", "🐮", "Moo moo!", "Sunny Farm"),
            AnimalInfo("Horse", "🐴", "Neigh neigh!", "Open Field"),
            AnimalInfo("Bird", "🐦", "Chirp chirp!", "Sky"),
            AnimalInfo("Pig", "🐷", "Oink oink!", "Muddy Farm")
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

    fun playAnimal(animal: AnimalInfo) {
        val message = "${animal.name}! The ${animal.name} says ${animal.sound}."
        textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "animal_${animal.name}")
        coroutineScope.launch {
            progressRepository.recordCompletion("animal_world", 1)
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
                    text = "Animal World 🐾",
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

        // Hero feature card for the selected animal
        val currentAnimal = animals[selectedIndex]
        val (cardBg, cardText) = CategoryColors[selectedIndex % CategoryColors.size]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(cardBg)
                .clickable { playAnimal(currentAnimal) }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentAnimal.emoji,
                    fontSize = 72.sp
                )
                Text(
                    text = currentAnimal.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = cardText,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "\"${currentAnimal.sound}\"",
                    style = MaterialTheme.typography.titleLarge,
                    color = cardText,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Habitat: ${currentAnimal.habitat}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cardText.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 2.dp)
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
                        contentDescription = "Listen",
                        tint = cardText,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Listen to sound!",
                        style = MaterialTheme.typography.labelLarge,
                        color = cardText
                    )
                }
            }
        }

        // Animal grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(animals) { index, animal ->
                val isSelected = index == selectedIndex
                val (itemBg, itemTextColor) = CategoryColors[index % CategoryColors.size]

                Box(
                    modifier = Modifier
                        .height(96.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isSelected) ToddlerPrimary else itemBg)
                        .clickable {
                            selectedIndex = index
                            playAnimal(animal)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = animal.emoji,
                            fontSize = 32.sp
                        )
                        Text(
                            text = animal.name,
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
