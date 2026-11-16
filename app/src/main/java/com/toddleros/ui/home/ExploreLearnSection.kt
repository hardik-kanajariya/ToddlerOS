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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelMintDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelSkyDark
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark

@Composable
fun ExploreLearnSection(
    onOpenAlphabet: () -> Unit,
    onOpenNumbers: () -> Unit,
    onOpenAnimals: () -> Unit,
    onOpenColorsShapes: () -> Unit,
    onOpenFruitsVeggies: () -> Unit,
    onOpenVehicles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ExploreItem("Alphabet Adventure", "🔤", "A to Z letters & phonics", PastelSky, PastelSkyDark, onOpenAlphabet),
        ExploreItem("Number Garden", "🌻", "Count flowers & fruit", PastelMint, PastelMintDark, onOpenNumbers),
        ExploreItem("Animal World", "🐾", "Discover sounds & habitats", PastelYellow, PastelYellowDark, onOpenAnimals),
        ExploreItem("Colors & Shapes", "🎨", "Shape matching quests", PastelCoral, PastelCoralDark, onOpenColorsShapes),
        ExploreItem("Fruits & Veggies", "🍎", "Fresh produce & colors", PastelPeach, PastelPeachDark, onOpenFruitsVeggies),
        ExploreItem("Vehicles & Transport", "🚀", "Cars, trains & rockets", PastelLavender, PastelLavenderDark, onOpenVehicles)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size) { index ->
            val item = items[index]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(item.bgColor)
                    .clickable { item.onClick() }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = item.emoji, fontSize = 42.sp)
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = item.textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = item.textColor.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

private data class ExploreItem(
    val title: String,
    val emoji: String,
    val subtitle: String,
    val bgColor: Color,
    val textColor: Color,
    val onClick: () -> Unit
)
