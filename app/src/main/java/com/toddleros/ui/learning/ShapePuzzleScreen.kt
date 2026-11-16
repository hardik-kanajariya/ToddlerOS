package com.toddleros.ui.learning

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.PastelCoral
import com.toddleros.core.theme.PastelMint
import com.toddleros.core.theme.PastelSky
import com.toddleros.core.theme.PastelYellow
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.launch
import java.util.Locale

data class PuzzlePiece(val id: Int, val name: String, val emoji: String, val color: Color, var isPlaced: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapePuzzleScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val pieces = remember {
        mutableStateListOf(
            PuzzlePiece(1, "Circle", "🔴", PastelCoral),
            PuzzlePiece(2, "Square", "🟦", PastelSky),
            PuzzlePiece(3, "Star", "⭐", PastelYellow),
            PuzzlePiece(4, "Heart", "💚", PastelMint)
        )
    }

    var selectedPiece by remember { mutableStateOf<PuzzlePiece?>(null) }
    var allPlaced by remember { mutableStateOf(false) }

    fun resetPuzzle() {
        pieces.forEachIndexed { i, p -> pieces[i] = p.copy(isPlaced = false) }
        selectedPiece = null
        allPlaced = false
    }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.speak("Let's solve the shape puzzle, Vasu!", TextToSpeech.QUEUE_FLUSH, null, "puzzle_intro")
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
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
                    text = "Shape Puzzle 🧩",
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
            actions = {
                IconButton(onClick = { resetPuzzle() }) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Reset",
                        tint = ToddlerInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Puzzle Board (Outlines)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(ToddlerSurface)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (allPlaced) "🎉 Puzzle Solved, Vasu! 🎉" else "Match the shapes into the slots!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ToddlerInk
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        pieces.forEachIndexed { index, p ->
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (p.isPlaced) p.color else Color.LightGray.copy(alpha = 0.3f))
                                    .clickable {
                                        if (selectedPiece?.id == p.id && !p.isPlaced) {
                                            pieces[index] = p.copy(isPlaced = true)
                                            selectedPiece = null
                                            textToSpeech?.speak("Great job, Vasu! That's the ${p.name}!", TextToSpeech.QUEUE_FLUSH, null, "placed")

                                            if (pieces.all { it.isPlaced }) {
                                                allPlaced = true
                                                textToSpeech?.speak("You completed the puzzle, Vasu! Super star!", TextToSpeech.QUEUE_ADD, null, "win")
                                                coroutineScope.launch {
                                                    progressRepository.recordCompletion("shape_puzzle", 3)
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (p.isPlaced) {
                                    Text(text = p.emoji, fontSize = 36.sp)
                                } else {
                                    Text(text = "🔲", fontSize = 28.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Piece tray
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(ToddlerSurface)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tap a piece below, then tap its slot above! 👇",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ToddlerInk,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    pieces.filter { !it.isPlaced }.forEach { piece ->
                        val isSelected = selectedPiece?.id == piece.id
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) ToddlerPrimary else piece.color)
                                .clickable {
                                    selectedPiece = piece
                                    textToSpeech?.speak(piece.name, TextToSpeech.QUEUE_FLUSH, null, "select_${piece.name}")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = piece.emoji, fontSize = 32.sp)
                        }
                    }
                }
            }

            if (allPlaced) {
                Button(
                    onClick = { resetPuzzle() },
                    colors = ButtonDefaults.buttonColors(containerColor = ToddlerPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Play Again! 🌟", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Text(
                    text = if (selectedPiece != null) "Selected: ${selectedPiece!!.name}. Now tap the matching slot!" else "Choose a piece to place.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ToddlerInk,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
