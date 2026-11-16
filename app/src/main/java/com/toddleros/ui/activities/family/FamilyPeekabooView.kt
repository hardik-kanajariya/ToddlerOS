package com.toddleros.ui.activities.family

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.family.FamilyPhoto
import kotlinx.coroutines.launch

@Composable
fun FamilyPeekabooView(
    photos: List<FamilyPhoto>,
    onSpotRevealed: (FamilyPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    val revealedStates = remember { mutableStateMapOf<String, Boolean>() }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tap a surprise to find who is hiding! 🙈",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = ToddlerInk.copy(alpha = 0.75f),
                modifier = Modifier.weight(1f)
            )

            // Hide all covers again
            if (revealedStates.isNotEmpty()) {
                Button(
                    onClick = { revealedStates.clear() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ToddlerSurface,
                        contentColor = ToddlerInk
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(44.dp)
                        .shadow(2.dp, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Hide all again",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Play Again",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            itemsIndexed(
                items = photos,
                key = { _, photo -> photo.id }
            ) { _, photo ->
                val isRevealed = revealedStates[photo.id] == true

                PeekabooItemCard(
                    photo = photo,
                    isRevealed = isRevealed,
                    onClick = {
                        revealedStates[photo.id] = true
                        onSpotRevealed(photo)
                    }
                )
            }
        }
    }
}

@Composable
private fun PeekabooItemCard(
    photo: FamilyPhoto,
    isRevealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bounceScale = remember { Animatable(1f) }

    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            bounceScale.animateTo(1.1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            bounceScale.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(bounceScale.value)
            .shadow(6.dp, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(photo.cardColor)
            .border(3.dp, Color.White, RoundedCornerShape(28.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                coroutineScope.launch {
                    bounceScale.animateTo(0.92f, spring(stiffness = Spring.StiffnessMedium))
                    bounceScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
                onClick()
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isRevealed,
            transitionSpec = {
                (scaleIn(initialScale = 0.6f) + fadeIn()) togetherWith (scaleOut(targetScale = 0.6f) + fadeOut())
            },
            label = "peekabooReveal"
        ) { revealed ->
            if (revealed) {
                // Revealed Photo State
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .shadow(4.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(ToddlerSurface)
                            .border(3.dp, ToddlerPaper, RoundedCornerShape(20.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(photo.assetPath)
                                .crossfade(180)
                                .build(),
                            contentDescription = photo.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Top Heart Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(ToddlerSurface.copy(alpha = 0.95f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💖", fontSize = 14.sp)
                        }
                    }

                    Text(
                        text = "Peekaboo! 🥳",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = photo.textColor,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Cover State (Hiding behind playful object)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.aspectRatio(0.88f)
                ) {
                    FamilyPeekCover(
                        coverType = photo.coverType,
                        size = 88.dp
                    )

                    Text(
                        text = "Tap me! ✨",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = photo.textColor.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
