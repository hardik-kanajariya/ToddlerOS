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
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelCoralDark
import com.toddleros.core.theme.PastelPeach
import com.toddleros.core.theme.PastelPeachDark

@Composable
fun PlayCreateSection(
    onOpenMemoryMatch: () -> Unit,
    onOpenShapePuzzle: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(PastelPeach)
                    .clickable { onOpenMemoryMatch() }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🧩", fontSize = 48.sp)
                    Text(
                        text = "Memory Matching",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PastelPeachDark,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = "Flip cards and find pairs of cute animal friends",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PastelPeachDark.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(PastelCoral)
                    .clickable { onOpenShapePuzzle() }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔲", fontSize = 48.sp)
                    Text(
                        text = "Shape Puzzle Challenge",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PastelCoralDark,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        text = "Fit colorful shapes into their matching slots",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PastelCoralDark.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
