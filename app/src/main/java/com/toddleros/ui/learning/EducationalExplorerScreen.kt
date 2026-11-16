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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.media.ImageItemAsset
import com.toddleros.core.media.MediaAssetRegistry
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
import com.toddleros.core.theme.PastelTeal
import com.toddleros.core.theme.PastelTealDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerPrimaryOn
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EducationalExplorerScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    val categories = remember { MediaAssetRegistry.imageCategories.keys.toList() }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "Animals") }

    val categoryColors = remember {
        mapOf(
            "Animals" to (PastelYellow to PastelYellowDark),
            "Fruits" to (PastelCoral to PastelCoralDark),
            "Alphabet" to (PastelSky to PastelSkyDark),
            "Numbers" to (PastelMint to PastelMintDark),
            "Colors" to (PastelLavender to PastelLavenderDark),
            "Shapes" to (PastelTeal to PastelTealDark),
            "Nature" to (PastelPeach to PastelPeachDark),
            "Transportation" to (PastelSky to PastelSkyDark)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
            ttsManager.release()
        }
    }

    fun handleItemTap(item: ImageItemAsset) {
        audioEngine.playPianoNote(523.25f, 300)
        coroutineScope.launch {
            delay(100L)
            ttsManager.speakPlayful(item.label)
        }
    }

    val items = MediaAssetRegistry.imageCategories[selectedCategory] ?: emptyList()
    val (cardBg, cardText) = categoryColors[selectedCategory] ?: (PastelSky to PastelSkyDark)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Discover & Learn 🌟",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        // Category Pills Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ToddlerPrimary else ToddlerSurface)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) ToddlerPrimaryOn else ToddlerInk
                    )
                }
            }
        }

        Text(
            text = "Touch any picture to hear its name! 🎨",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        // Items Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                EducationalImageCard(
                    item = item,
                    bgColor = cardBg,
                    textColor = cardText,
                    onClick = { handleItemTap(item) }
                )
            }
        }
    }
}

@Composable
private fun EducationalImageCard(
    item: ImageItemAsset,
    bgColor: Color,
    textColor: Color,
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
            .background(bgColor)
            .border(3.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(28.dp))
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(ToddlerSurface)
                    .border(3.dp, ToddlerPaper, RoundedCornerShape(20.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/${item.svgPath}")
                        .crossfade(true)
                        .build(),
                    contentDescription = item.label,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
