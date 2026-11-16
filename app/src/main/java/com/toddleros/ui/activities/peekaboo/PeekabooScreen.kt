package com.toddleros.ui.activities.peekaboo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.audio.ToddlerAudioEngine
import com.toddleros.core.audio.ToddlerTtsManager
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.ui.activities.animals.AnimalArtwork
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PeekSpot(
    val id: String,
    val name: String,
    val coverType: CoverType,
    val hiddenAnimal: String,
    val cardColor: Color
)

enum class CoverType {
    BUSH,
    GIFT_BOX,
    CLOUD,
    BLANKET
}

@Composable
fun PeekabooScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioEngine = remember { ToddlerAudioEngine() }
    val ttsManager = remember { ToddlerTtsManager(context) }
    var isMuted by remember { mutableStateOf(false) }

    var revealedSpotIndex by remember { mutableIntStateOf(-1) }

    val spots = remember {
        listOf(
            PeekSpot("spot1", "Garden Bush", CoverType.BUSH, "cat", PastelMint),
            PeekSpot("spot2", "Gift Box", CoverType.GIFT_BOX, "dog", PastelYellow),
            PeekSpot("spot3", "Fluffy Cloud", CoverType.CLOUD, "duck", PastelLavender),
            PeekSpot("spot4", "Cozy Blanket", CoverType.BLANKET, "lion", PastelCoral)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.release()
            ttsManager.release()
        }
    }

    fun onSpotTap(index: Int) {
        revealedSpotIndex = index
        audioEngine.playPeekabooChime()

        val spot = spots[index]
        val phrases = listOf(
            "Peekaboo! Here is ${spot.hiddenAnimal}!",
            "I see you, ${spot.hiddenAnimal}!",
            "Peekaboo! Hello friend!"
        )
        val phrase = phrases.random()

        coroutineScope.launch {
            delay(250L)
            ttsManager.speakPlayful(phrase)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Peekaboo! 🙈",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioEngine.setMuted(isMuted)
                ttsManager.setMuted(isMuted)
            }
        )

        Text(
            text = "Tap any object to find who is hiding!",
            style = MaterialTheme.typography.titleMedium,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PeekSpotCard(
                    spot = spots[0],
                    isRevealed = revealedSpotIndex == 0,
                    onClick = { onSpotTap(0) },
                    modifier = Modifier.weight(1f)
                )
                PeekSpotCard(
                    spot = spots[1],
                    isRevealed = revealedSpotIndex == 1,
                    onClick = { onSpotTap(1) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PeekSpotCard(
                    spot = spots[2],
                    isRevealed = revealedSpotIndex == 2,
                    onClick = { onSpotTap(2) },
                    modifier = Modifier.weight(1f)
                )
                PeekSpotCard(
                    spot = spots[3],
                    isRevealed = revealedSpotIndex == 3,
                    onClick = { onSpotTap(3) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PeekSpotCard(
    spot: PeekSpot,
    isRevealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            scale.animateTo(1.08f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            scale.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .scale(scale.value)
            .clip(RoundedCornerShape(32.dp))
            .background(spot.cardColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRevealed) {
            // Character pop-out state
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimalArtwork(
                    animalId = spot.hiddenAnimal,
                    size = 96.dp
                )
                Text(
                    text = "Peekaboo!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ToddlerInk,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            // Hiding cover object
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CoverArtwork(coverType = spot.coverType)
                Text(
                    text = "Tap me! ✨",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = ToddlerInk.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun CoverArtwork(coverType: CoverType) {
    Canvas(modifier = Modifier.size(100.dp)) {
        val w = size.width
        val h = size.height

        when (coverType) {
            CoverType.BUSH -> {
                val greenDark = Color(0xFF15803D)
                val greenLight = Color(0xFF22C55E)
                drawCircle(greenDark, radius = w * 0.35f, center = Offset(w * 0.35f, h * 0.5f))
                drawCircle(greenDark, radius = w * 0.35f, center = Offset(w * 0.65f, h * 0.5f))
                drawCircle(greenLight, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.38f))
                // Berries
                drawCircle(Color(0xFFEF4444), radius = w * 0.05f, center = Offset(w * 0.42f, h * 0.45f))
                drawCircle(Color(0xFFEF4444), radius = w * 0.05f, center = Offset(w * 0.58f, h * 0.42f))
            }
            CoverType.GIFT_BOX -> {
                val boxRed = Color(0xFFEF4444)
                val ribbonYellow = Color(0xFFFBBF24)
                drawRoundRect(boxRed, topLeft = Offset(w * 0.18f, h * 0.3f), size = Size(w * 0.64f, h * 0.55f), cornerRadius = CornerRadius(16f))
                // Ribbon
                drawRect(ribbonYellow, topLeft = Offset(w * 0.44f, h * 0.3f), size = Size(w * 0.12f, h * 0.55f))
                drawRect(ribbonYellow, topLeft = Offset(w * 0.18f, h * 0.52f), size = Size(w * 0.64f, h * 0.12f))
                // Bow
                drawCircle(ribbonYellow, radius = w * 0.1f, center = Offset(w * 0.4f, h * 0.26f))
                drawCircle(ribbonYellow, radius = w * 0.1f, center = Offset(w * 0.6f, h * 0.26f))
            }
            CoverType.CLOUD -> {
                val cloudWhite = Color.White
                drawCircle(cloudWhite, radius = w * 0.24f, center = Offset(w * 0.32f, h * 0.52f))
                drawCircle(cloudWhite, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.42f))
                drawCircle(cloudWhite, radius = w * 0.24f, center = Offset(w * 0.68f, h * 0.52f))
                drawRoundRect(cloudWhite, topLeft = Offset(w * 0.22f, h * 0.52f), size = Size(w * 0.56f, h * 0.25f), cornerRadius = CornerRadius(20f))
            }
            CoverType.BLANKET -> {
                val blanketColor = Color(0xFFEC4899)
                val dotColor = Color(0xFFFDE047)
                drawRoundRect(blanketColor, topLeft = Offset(w * 0.15f, h * 0.28f), size = Size(w * 0.7f, h * 0.56f), cornerRadius = CornerRadius(24f))
                // Polka dots
                drawCircle(dotColor, radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.42f))
                drawCircle(dotColor, radius = w * 0.05f, center = Offset(w * 0.65f, h * 0.42f))
                drawCircle(dotColor, radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.6f))
            }
        }
    }
}
