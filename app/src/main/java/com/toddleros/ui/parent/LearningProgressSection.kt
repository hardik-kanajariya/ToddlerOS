package com.toddleros.ui.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.PastelYellowDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerInkSecondary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository

@Composable
fun LearningProgressSection(
    progressRepository: ProgressRepository,
    modifier: Modifier = Modifier
) {
    val totalStars by progressRepository.getTotalStars().collectAsState(initial = 0)

    val activities = listOf(
        ProgressCardData("Alphabet Adventure", "🔤", "Letter recognition and phonics"),
        ProgressCardData("Number Garden", "🌻", "Visual counting & numbers"),
        ProgressCardData("Animal World", "🐾", "Animal sounds & habitats"),
        ProgressCardData("Colors & Shapes", "🎨", "Shape & color matching quests"),
        ProgressCardData("Creative Studio", "🖍️", "Finger drawing & artwork"),
        ProgressCardData("Memory Match", "🧩", "Animal pair matching")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Total stars hero banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(PastelYellow)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "⭐", fontSize = 48.sp)
                Text(
                    text = "$totalStars Stars Earned",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PastelYellowDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Vasu earns stars by exploring and playing educational activities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PastelYellowDark.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Text(
            text = "Learning Activity Overview",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ToddlerInk,
            modifier = Modifier.padding(top = 8.dp)
        )

        activities.forEach { act ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ToddlerSurface)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = act.emoji, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = act.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = ToddlerInk
                    )
                    Text(
                        text = act.desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = ToddlerInkSecondary
                    )
                }
            }
        }
    }
}

private data class ProgressCardData(val title: String, val emoji: String, val desc: String)
