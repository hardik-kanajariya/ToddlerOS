package com.toddleros.ui.activities.animals

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AnimalModel(
    val id: String,
    val name: String,
    val soundWord: String,
    val bgColor: Color,
    val textColor: Color
)

@Composable
fun AnimalSoundsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }
    var activeAnimalIndex by remember { mutableIntStateOf(-1) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val animals = remember {
        listOf(
            AnimalModel("dog", "Dog", "Woof!", PastelYellow, PastelYellowDark),
            AnimalModel("cat", "Cat", "Meow!", PastelCoral, PastelCoralDark),
            AnimalModel("cow", "Cow", "Moo!", PastelMint, PastelMintDark),
            AnimalModel("lion", "Lion", "Roar!", PastelPeach, PastelPeachDark),
            AnimalModel("duck", "Duck", "Quack!", PastelSky, PastelSkyDark),
            AnimalModel("elephant", "Elephant", "Pawoo!", PastelLavender, PastelLavenderDark),
            AnimalModel("sheep", "Sheep", "Baa!", PastelMint, PastelMintDark),
            AnimalModel("monkey", "Monkey", "Ooh ooh!", PastelYellow, PastelYellowDark)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
            ttsManager.release()
        }
    }

    fun playAnimal(index: Int) {
        lastInteractionTime = System.currentTimeMillis()
        activeAnimalIndex = index
        val animal = animals[index]

        if (!isMuted) {
            audioEngine.playAnimalAcousticSound(animal.id)
            coroutineScope.launch {
                delay(300L)
                ttsManager.speakPlayful("${animal.name}! ${animal.soundWord}")
            }
        }
    }

    // Auto-demonstration exploration nudge if toddler is watching passively for 9 seconds
    LaunchedEffect(lastInteractionTime) {
        delay(9000L)
        val randomIndex = (animals.indices).random()
        playAnimal(randomIndex)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Animal Sounds 🐾",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(animals) { index, animal ->
                val isSelected = index == activeAnimalIndex
                val scale = remember { Animatable(1f) }

                LaunchedEffect(isSelected) {
                    if (isSelected) {
                        scale.animateTo(
                            targetValue = 1.12f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                        )
                        scale.animateTo(1f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                        activeAnimalIndex = -1
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .scale(scale.value)
                        .clip(RoundedCornerShape(32.dp))
                        .background(animal.bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            playAnimal(index)
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimalArtwork(
                            animalId = animal.id,
                            size = 86.dp
                        )
                        Text(
                            text = animal.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = animal.textColor,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
