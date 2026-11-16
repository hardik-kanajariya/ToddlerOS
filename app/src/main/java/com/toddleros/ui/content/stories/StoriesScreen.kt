package com.toddleros.ui.content.stories

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.MediaType
import com.toddleros.domain.model.StoryBook
import com.toddleros.domain.model.StoryPage
import com.toddleros.domain.repository.MediaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesScreen(
    mediaRepository: MediaRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localStoryItems by mediaRepository.getMediaItems(MediaType.STORY).collectAsState(initial = emptyList())
    var activeStory by remember { mutableStateOf<StoryBook?>(null) }

    val defaultStories = remember {
        listOf(
            StoryBook(
                id = "vasu_sun_cloud",
                title = "Vasu and the Cheerful Sun",
                pages = listOf(
                    StoryPage(1, "Good morning, Vasu! The warm sun smiled brightly down at the garden."),
                    StoryPage(2, "A friendly little cloud waved hello and brought a gentle, fresh breeze."),
                    StoryPage(3, "Together, they made a giant sparkling rainbow right above Vasu!")
                )
            ),
            StoryBook(
                id = "vasu_forest_friends",
                title = "Vasu's Forest Friends",
                pages = listOf(
                    StoryPage(1, "Vasu took a sunny walk into the forest and met a bouncy little bunny."),
                    StoryPage(2, "Gentle Bear shared sweet red apples with Vasu and all the woodland friends."),
                    StoryPage(3, "They all laughed, played, and danced happily under the tall green trees.")
                )
            )
        )
    }

    val allStories = remember(localStoryItems) {
        val converted = localStoryItems.map { item ->
            StoryBook(
                id = "custom_${item.id}",
                title = item.title,
                pages = listOf(
                    StoryPage(1, item.title, imageUri = item.uriString)
                )
            )
        }
        defaultStories + converted
    }

    if (activeStory != null) {
        StoryReaderScreen(
            story = activeStory!!,
            onClose = { activeStory = null }
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
                    text = "Vasu's Stories 📚",
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(allStories, key = { it.id }) { story ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(PastelPeach)
                        .clickable { activeStory = story }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "📖", fontSize = 38.sp)
                        Text(
                            text = story.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = PastelPeachDark,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
