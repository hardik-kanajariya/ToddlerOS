package com.toddleros.ui.learning

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.media.AnimalAsset
import com.toddleros.core.media.MediaAssetRegistry
import com.toddleros.core.media.ToddlerMediaAudioPlayer
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimalWorldScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioPlayer = remember { ToddlerMediaAudioPlayer(context) }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    val animalList = remember { MediaAssetRegistry.animals }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
            ttsManager.release()
        }
    }

    fun handleAnimalTap(animal: AnimalAsset) {
        audioPlayer.playAsset(animal.soundFile)
        coroutineScope.launch {
            delay(1200L)
            ttsManager.speakPlayful("${animal.name}! Look at the friendly ${animal.name}!")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Animal World 🐾",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioPlayer.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        Text(
            text = "Touch any animal to hear its real sound! 🦁",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = animalList,
                key = { it.id }
            ) { animal ->
                AnimalCardItem(
                    animal = animal,
                    onClick = { handleAnimalTap(animal) }
                )
            }
        }
    }
}

@Composable
private fun AnimalCardItem(
    animal: AnimalAsset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    fun handleTap() {
        coroutineScope.launch {
            scale.animateTo(0.92f, spring(stiffness = Spring.StiffnessMedium))
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        onClick()
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .shadow(6.dp, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(animal.cardColor)
            .border(3.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                handleTap()
            }
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Illustrated / SVG container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(ToddlerSurface)
                    .border(3.dp, ToddlerPaper, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (animal.imageFile != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("file:///android_asset/${animal.imageFile}")
                            .crossfade(true)
                            .build(),
                        contentDescription = animal.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                } else {
                    Text(
                        text = animal.emoji,
                        fontSize = 52.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Sound icon badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ToddlerSurface.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔊", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = animal.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = animal.textColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = animal.group,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = animal.textColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
