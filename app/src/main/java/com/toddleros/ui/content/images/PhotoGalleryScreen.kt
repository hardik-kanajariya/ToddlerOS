package com.toddleros.ui.content.images

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.repository.MediaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    mediaRepository: MediaRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photos by mediaRepository.getMediaItems(MediaType.PHOTO).collectAsState(initial = emptyList())
    var selectedIndex by remember { mutableIntStateOf(-1) }

    if (selectedIndex in photos.indices) {
        val currentPhoto = photos[selectedIndex]
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = Uri.parse(currentPhoto.uriString),
                contentDescription = currentPhoto.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { selectedIndex = -1 },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
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
                    text = "Vasu's Photos 📸",
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

        if (photos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📸", fontSize = 52.sp)
                    Text(
                        text = "No pictures yet!",
                        style = MaterialTheme.typography.titleLarge,
                        color = ToddlerInk,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Ask a grown-up to add family photos or animal pictures for Vasu from the Parent Dashboard.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ToddlerInkSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(photos, key = { _, it -> it.id }) { index, item ->
                    Box(
                        modifier = Modifier
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ToddlerSurface)
                            .clickable { selectedIndex = index }
                    ) {
                        AsyncImage(
                            model = Uri.parse(item.uriString),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
