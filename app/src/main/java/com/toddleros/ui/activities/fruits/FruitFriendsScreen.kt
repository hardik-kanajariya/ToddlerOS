package com.toddleros.ui.activities.fruits

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

data class FruitModel(
    val id: String,
    val name: String,
    val funPhrase: String,
    val bgColor: Color,
    val textColor: Color
)

@Composable
fun FruitFriendsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    var activeFruitIndex by remember { mutableIntStateOf(-1) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val fruits = remember {
        listOf(
            FruitModel("apple", "Apple", "Crunchy red apple!", PastelCoral, PastelCoralDark),
            FruitModel("banana", "Banana", "Sweet yellow banana!", PastelYellow, PastelYellowDark),
            FruitModel("orange", "Orange", "Juicy orange!", PastelPeach, PastelPeachDark),
            FruitModel("mango", "Mango", "Golden sweet mango!", PastelYellow, PastelYellowDark),
            FruitModel("watermelon", "Watermelon", "Cool yummy watermelon!", PastelMint, PastelMintDark),
            FruitModel("grapes", "Grapes", "Little purple grapes!", PastelLavender, PastelLavenderDark),
            FruitModel("strawberry", "Strawberry", "Fresh sweet strawberry!", PastelCoral, PastelCoralDark)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
            ttsManager.release()
        }
    }

    fun playFruit(index: Int) {
        lastInteractionTime = System.currentTimeMillis()
        activeFruitIndex = index
        val fruit = fruits[index]

        if (!isMuted) {
            audioEngine.playPianoNote(523.25f, 250)
            coroutineScope.launch {
                delay(200L)
                ttsManager.speakPlayful("${fruit.name}! ${fruit.funPhrase}")
            }
        }
    }

    // Auto-demonstration exploration nudge after 10 seconds of idle
    LaunchedEffect(lastInteractionTime) {
        delay(10000L)
        val randomIndex = (fruits.indices).random()
        playFruit(randomIndex)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Fruit Friends 🍎",
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
            itemsIndexed(fruits) { index, fruit ->
                val isSelected = index == activeFruitIndex
                val scale = remember { Animatable(1f) }

                LaunchedEffect(isSelected) {
                    if (isSelected) {
                        scale.animateTo(
                            targetValue = 1.12f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                        )
                        scale.animateTo(1f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                        activeFruitIndex = -1
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .scale(scale.value)
                        .clip(RoundedCornerShape(32.dp))
                        .background(fruit.bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            playFruit(index)
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        FruitArtwork(
                            fruitId = fruit.id,
                            size = 86.dp
                        )
                        Text(
                            text = fruit.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = fruit.textColor,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
