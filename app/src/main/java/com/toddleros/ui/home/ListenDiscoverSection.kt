package com.toddleros.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark

@Composable
fun ListenDiscoverSection(
    onOpenMusic: () -> Unit,
    onOpenVideos: () -> Unit,
    onOpenStories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ListenItem("Vasu's Tunes", "🎵", "Songs, nursery rhymes & melodies", PastelYellow, PastelYellowDark, onOpenMusic),
        ListenItem("Vasu's Videos", "🎬", "Watch offline children's videos", PastelSky, PastelSkyDark, onOpenVideos),
        ListenItem("Storybooks", "📚", "Picture books with cheerful read-aloud", PastelPeach, PastelPeachDark, onOpenStories)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size) { index ->
            val item = items[index]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(item.bgColor)
                    .clickable { item.onClick() }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = item.emoji, fontSize = 38.sp)
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = item.textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = item.textColor.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

private data class ListenItem(
    val title: String,
    val emoji: String,
    val subtitle: String,
    val bgColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color,
    val onClick: () -> Unit
)
