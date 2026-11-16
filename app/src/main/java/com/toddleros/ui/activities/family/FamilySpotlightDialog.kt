package com.toddleros.ui.activities.family

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.family.FamilyPhoto

@Composable
fun FamilySpotlightDialog(
    photo: FamilyPhoto,
    onDismiss: () -> Unit,
    onReplayAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")

    // Gentle floating offset for decorative elements
    val floatOffset = infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingSparkles"
    )

    // Pulsing glow for celebration
    val pulseScale = infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            // Main card container
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(36.dp))
                    .background(ToddlerPaper)
                    .border(4.dp, Color.White, RoundedCornerShape(36.dp))
                    .shadow(16.dp, RoundedCornerShape(36.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Prevent dismiss when tapping inside the card
                    }
                    .padding(20.dp)
            ) {
                // Floating Sparkles and Hearts
                Text(
                    text = "💖",
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 10.dp, y = (floatOffset.value).dp)
                )
                Text(
                    text = "✨",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-60).dp, y = (-floatOffset.value).dp)
                )
                Text(
                    text = "🌟",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 12.dp, y = (-floatOffset.value / 2).dp)
                )

                // Large Close Button (Top Right)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(ToddlerSurface)
                        .border(2.dp, Color(0xFFE2E8F0), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = ToddlerInk,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // Prominent Photo with Golden / Pastel Border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .aspectRatio(1f)
                            .scale(pulseScale.value)
                            .shadow(12.dp, RoundedCornerShape(30.dp))
                            .clip(RoundedCornerShape(30.dp))
                            .background(photo.cardColor)
                            .border(6.dp, Color.White, RoundedCornerShape(30.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onReplayAudio()
                            }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(photo.assetPath)
                                .crossfade(true)
                                .build(),
                            contentDescription = photo.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Corner emoji badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ToddlerSurface.copy(alpha = 0.95f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = photo.emoji, fontSize = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = photo.displayName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = ToddlerInk,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle / Spoken message preview
                    Text(
                        text = "\"${photo.spokenGreeting}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Replay Voice Greeting Button (Toddler accessible touch target: 60dp high)
                    Button(
                        onClick = onReplayAudio,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(60.dp)
                            .shadow(4.dp, RoundedCornerShape(30.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = photo.cardColor,
                            contentColor = photo.textColor
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = photo.textColor
                            )
                            Text(
                                text = "Hear Voice Again ✨",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = photo.textColor
                            )
                        }
                    }
                }
            }
        }
    }
}
