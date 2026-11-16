package com.toddleros.ui.content.music

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkMuted
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.MediaItem
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.repository.MediaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicLibraryScreen(
    mediaRepository: MediaRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioItems by mediaRepository.getMediaItems(MediaType.AUDIO).collectAsState(initial = emptyList())

    var currentIndex by remember { mutableIntStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    var isShuffle by remember { mutableStateOf(false) }
    var isRepeat by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        if (isRepeat) {
                            seekTo(0)
                            play()
                        }
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    fun playTrack(index: Int) {
        if (index in audioItems.indices) {
            currentIndex = index
            val item = audioItems[index]
            exoPlayer.setMediaItem(ExoMediaItem.fromUri(Uri.parse(item.uriString)))
            exoPlayer.prepare()
            exoPlayer.play()
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
                    text = "Vasu's Tunes 🎵",
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

        if (audioItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎵", fontSize = 52.sp)
                    Text(
                        text = "No songs yet!",
                        style = MaterialTheme.typography.titleLarge,
                        color = ToddlerInk,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Ask a grown-up to add cheerful songs or lullabies for Vasu from the Parent Dashboard.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ToddlerInkSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(audioItems, key = { _, it -> it.id }) { index, item ->
                        val isCurrent = index == currentIndex
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isCurrent) PastelYellow else ToddlerSurface)
                                .clickable { playTrack(index) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isCurrent && isPlaying) "🎶" else "🎵",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isCurrent) PastelYellowDark else ToddlerInk
                                )
                                Text(
                                    text = item.artistOrAuthor ?: "Local Audio",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isCurrent) PastelYellowDark else ToddlerInkSecondary
                                )
                            }
                        }
                    }
                }

                if (currentIndex in audioItems.indices) {
                    val currentSong = audioItems[currentIndex]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                            .background(ToddlerSurface)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentSong.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = ToddlerInk,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { isShuffle = !isShuffle }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Shuffle,
                                    contentDescription = "Shuffle",
                                    tint = if (isShuffle) ToddlerPrimary else ToddlerInkMuted
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (currentIndex > 0) playTrack(currentIndex - 1)
                                },
                                modifier = Modifier.size(52.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SkipPrevious,
                                    contentDescription = "Previous",
                                    tint = ToddlerInk,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(PastelPeach)
                                    .clickable {
                                        if (isPlaying) exoPlayer.pause() else exoPlayer.play()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = PastelPeachDark,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (currentIndex < audioItems.size - 1) playTrack(currentIndex + 1)
                                },
                                modifier = Modifier.size(52.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.SkipNext,
                                    contentDescription = "Next",
                                    tint = ToddlerInk,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            IconButton(
                                onClick = { isRepeat = !isRepeat }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Repeat,
                                    contentDescription = "Repeat",
                                    tint = if (isRepeat) ToddlerPrimary else ToddlerInkMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
