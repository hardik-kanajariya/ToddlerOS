package com.toddleros.ui.content.stories

import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.media.MediaAssetRegistry
import com.toddleros.core.media.StoryAsset
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.StoryBook
import com.toddleros.domain.model.StoryPage
import com.toddleros.ui.activities.common.ToddlerTopBar
import kotlinx.coroutines.launch

@Composable
fun StoriesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeStory by remember { mutableStateOf<StoryBook?>(null) }
    val stories = remember { MediaAssetRegistry.stories }

    if (activeStory != null) {
        StoryReaderScreen(
            story = activeStory!!,
            onClose = { activeStory = null }
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ToddlerPaper)
        ) {
            ToddlerTopBar(
                title = "Story Time 📚",
                onBack = onBack
            )

            Text(
                text = "Touch a storybook to read and listen! 📖",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = ToddlerInk.copy(alpha = 0.75f),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = stories,
                    key = { it.id }
                ) { story ->
                    StoryCardItem(
                        story = story,
                        onClick = {
                            activeStory = loadStoryBook(context, story)
                        }
                    )
                }
            }
        }
    }
}

private fun loadStoryBook(context: Context, storyAsset: StoryAsset): StoryBook {
    return try {
        val text = context.assets.open(storyAsset.markdownFile).bufferedReader().use { it.readText() }
        val lines = text.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("*") }

        val pages = lines.chunked(2).mapIndexed { index, chunk ->
            StoryPage(pageNumber = index + 1, text = chunk.joinToString(" "))
        }

        StoryBook(
            id = storyAsset.id,
            title = storyAsset.title,
            pages = if (pages.isNotEmpty()) pages else listOf(StoryPage(1, storyAsset.summary))
        )
    } catch (_: Exception) {
        StoryBook(
            id = storyAsset.id,
            title = storyAsset.title,
            pages = listOf(StoryPage(1, storyAsset.summary))
        )
    }
}

@Composable
private fun StoryCardItem(
    story: StoryAsset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            .background(story.cardColor)
            .border(3.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                handleTap()
            }
            .padding(14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .shadow(3.dp, CircleShape)
                    .clip(CircleShape)
                    .background(ToddlerSurface)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = story.emoji, fontSize = 34.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = story.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = story.textColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = story.summary,
                style = MaterialTheme.typography.bodySmall,
                color = story.textColor.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
