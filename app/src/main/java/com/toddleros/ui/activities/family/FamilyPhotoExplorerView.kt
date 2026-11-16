package com.toddleros.ui.activities.family

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun FamilyPhotoExplorerView(
    photos: List<FamilyPhoto>,
    onPhotoClick: (FamilyPhoto) -> Unit,
    onPhotoChanged: (FamilyPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    if (photos.isEmpty()) return

    val context = LocalContext.current
    var currentIndex by remember { mutableIntStateOf(0) }
    var slideDirection by remember { mutableIntStateOf(1) } // 1: next, -1: prev

    val currentPhoto = photos[currentIndex.coerceIn(photos.indices)]

    fun goToNext() {
        slideDirection = 1
        currentIndex = (currentIndex + 1) % photos.size
        onPhotoChanged(photos[currentIndex])
    }

    fun goToPrev() {
        slideDirection = -1
        currentIndex = if (currentIndex - 1 < 0) photos.size - 1 else currentIndex - 1
        onPhotoChanged(photos[currentIndex])
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Explore family pictures one by one! 🎠",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Large Animated Photo Centerpiece
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentPhoto,
                transitionSpec = {
                    if (slideDirection > 0) {
                        (slideInHorizontally(animationSpec = tween(250)) { it } + fadeIn()) togetherWith
                                (slideOutHorizontally(animationSpec = tween(250)) { -it } + fadeOut())
                    } else {
                        (slideInHorizontally(animationSpec = tween(250)) { -it } + fadeIn()) togetherWith
                                (slideOutHorizontally(animationSpec = tween(250)) { it } + fadeOut())
                    }
                },
                label = "carouselSlide"
            ) { photo ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .aspectRatio(0.96f)
                        .shadow(12.dp, RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                        .background(photo.cardColor)
                        .border(5.dp, Color.White, RoundedCornerShape(32.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onPhotoClick(photo)
                        }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(22.dp))
                                .clip(RoundedCornerShape(22.dp))
                                .background(ToddlerSurface)
                                .border(4.dp, ToddlerPaper, RoundedCornerShape(22.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(photo.assetPath)
                                    .crossfade(200)
                                    .build(),
                                contentDescription = photo.displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Decorative emoji badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ToddlerSurface.copy(alpha = 0.95f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = photo.emoji, fontSize = 20.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = photo.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = photo.textColor,
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                                contentDescription = "Hear Greeting",
                                tint = photo.textColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Navigation Controls (Large 64dp Buttons for toddler thumbs)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            IconButton(
                onClick = { goToPrev() },
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(ToddlerSurface)
                    .border(2.dp, Color(0xFFCBD5E1), CircleShape)
                    .shadow(4.dp, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Previous Photo",
                    tint = ToddlerInk,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Photo counter badge (e.g. "3 / 21")
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(ToddlerSurface)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${currentIndex + 1} of ${photos.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ToddlerInk
                )
            }

            // Next Button
            IconButton(
                onClick = { goToNext() },
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(ToddlerSurface)
                    .border(2.dp, Color(0xFFCBD5E1), CircleShape)
                    .shadow(4.dp, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Next Photo",
                    tint = ToddlerInk,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
