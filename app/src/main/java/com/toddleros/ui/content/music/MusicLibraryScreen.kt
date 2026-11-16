package com.toddleros.ui.content.music

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.media.MediaAssetRegistry
import com.toddleros.core.media.SongAsset
import com.toddleros.core.media.ToddlerMediaAudioPlayer
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerPrimaryOn
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.launch

@Composable
fun MusicLibraryScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioPlayer = remember { ToddlerMediaAudioPlayer(context) }
    var isMuted by remember { mutableStateOf(false) }

    val songs = remember { MediaAssetRegistry.songs }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    fun playSong(index: Int) {
        if (index in songs.indices) {
            currentIndex = index
            val song = songs[index]
            audioPlayer.playAsset(song.audioFile, loop = false) {
                isPlaying = false
            }
            isPlaying = true
        }
    }

    fun togglePlayPause() {
        if (isPlaying) {
            audioPlayer.pause()
            isPlaying = false
        } else {
            if (audioPlayer.getCurrentPath() != null) {
                audioPlayer.resume()
                isPlaying = true
            } else {
                playSong(currentIndex)
            }
        }
    }

    val currentSong = songs[currentIndex.coerceIn(songs.indices)]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        ToddlerTopBar(
            title = "Tunes & Rhymes 🎵",
            onBack = onBack,
            isMuted = isMuted,
            onToggleMute = {
                isMuted = !isMuted
                audioPlayer.setMuted(isMuted)
            }
        )

        // Active Song Spotlight Card
        ActiveSongPlayerCard(
            song = currentSong,
            isPlaying = isPlaying,
            onTogglePlay = { togglePlayPause() },
            onReplay = { playSong(currentIndex) },
            onNext = { playSong((currentIndex + 1) % songs.size) },
            onPrev = { playSong(if (currentIndex - 1 < 0) songs.size - 1 else currentIndex - 1) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Text(
            text = "Touch any song to listen & sing along! 🎶",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = ToddlerInk.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        )

        // Song Catalog List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = songs,
                key = { _, song -> song.id }
            ) { index, song ->
                SongListItem(
                    song = song,
                    isCurrent = index == currentIndex,
                    isPlaying = isPlaying && index == currentIndex,
                    onClick = { playSong(index) }
                )
            }
        }
    }
}

@Composable
private fun ActiveSongPlayerCard(
    song: SongAsset,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onReplay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseMusic")
    val pulseScale = infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "musicPulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .background(song.cardColor)
            .border(3.dp, Color.White, RoundedCornerShape(32.dp))
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Song Emoji in bouncing circle
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .scale(if (isPlaying) pulseScale.value else 1f)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(ToddlerSurface)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = song.emoji, fontSize = 36.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isPlaying) "Now Playing 🎵" else "Selected Song",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = song.textColor.copy(alpha = 0.8f)
                    )

                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = song.textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = song.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = song.textColor.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toddler Controls (Prev, Big Play/Pause, Replay, Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPrev,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(ToddlerSurface)
                        .shadow(2.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "Previous Song",
                        tint = ToddlerInk,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Big Play / Pause Button (68dp touch target)
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(ToddlerPrimary)
                        .shadow(6.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = ToddlerPrimaryOn,
                        modifier = Modifier.size(42.dp)
                    )
                }

                IconButton(
                    onClick = onReplay,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(ToddlerSurface)
                        .shadow(2.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Replay,
                        contentDescription = "Replay Song",
                        tint = ToddlerInk,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(
                    onClick = onNext,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(ToddlerSurface)
                        .shadow(2.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Next Song",
                        tint = ToddlerInk,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SongListItem(
    song: SongAsset,
    isCurrent: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    fun handleTap() {
        coroutineScope.launch {
            scale.animateTo(0.95f, spring(stiffness = Spring.StiffnessMedium))
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        onClick()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale.value)
            .shadow(if (isCurrent) 4.dp else 1.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(if (isCurrent) song.cardColor else ToddlerSurface)
            .border(
                if (isCurrent) 2.dp else 1.dp,
                if (isCurrent) Color.White else Color(0xFFE2E8F0),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                handleTap()
            }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent) ToddlerSurface else song.cardColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = song.emoji, fontSize = 28.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) song.textColor else ToddlerInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = song.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCurrent) song.textColor.copy(alpha = 0.85f) else Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Play indicator icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent && isPlaying) ToddlerPrimary else ToddlerPaper),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCurrent && isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = if (isCurrent && isPlaying) ToddlerPrimaryOn else ToddlerInk,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
