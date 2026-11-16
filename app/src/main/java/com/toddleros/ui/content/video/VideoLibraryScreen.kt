package com.toddleros.ui.content.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.MediaItem
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.repository.MediaRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoLibraryScreen(
    mediaRepository: MediaRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val videos by mediaRepository.getMediaItems(MediaType.VIDEO).collectAsState(initial = emptyList())
    var activeVideo by remember { mutableStateOf<MediaItem?>(null) }

    if (activeVideo != null) {
        VideoPlayerView(
            item = activeVideo!!,
            onClose = { lastPos ->
                coroutineScope.launch {
                    mediaRepository.savePlaybackPosition(activeVideo!!.id, lastPos)
                }
                activeVideo = null
            }
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Vasu's Videos 🎬",
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

        if (videos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎬", fontSize = 52.sp)
                    Text(
                        text = "No videos yet!",
                        style = MaterialTheme.typography.titleLarge,
                        color = ToddlerInk,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Ask a grown-up to add fun videos for Vasu from the Parent Dashboard.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ToddlerInkSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(videos, key = { it.id }) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(PastelSky)
                            .clickable { activeVideo = item }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = "Play",
                                    tint = PastelSkyDark,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = PastelSkyDark,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
