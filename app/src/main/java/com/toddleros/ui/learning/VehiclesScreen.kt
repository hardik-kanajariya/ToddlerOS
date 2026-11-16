package com.toddleros.ui.learning

import android.speech.tts.TextToSpeech
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.VolumeUp
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddleros.core.theme.CategoryColors
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.launch
import java.util.Locale

data class VehicleItem(val name: String, val emoji: String, val soundDesc: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val vehicles = remember {
        listOf(
            VehicleItem("Car", "🚗", "Beep beep! Vroom vroom!"),
            VehicleItem("Train", "🚂", "Choo choo! Clickity clack!"),
            VehicleItem("Airplane", "✈️", "Whoosh high up in the clouds!"),
            VehicleItem("Boat", "⛵", "Splish splash sailing across the waves!"),
            VehicleItem("Bicycle", "🚲", "Ring ring! Pedal pedal!"),
            VehicleItem("Rocket", "🚀", "3, 2, 1, Blast off to the stars!"),
            VehicleItem("Fire Truck", "🚒", "Wee-woo wee-woo to the rescue!"),
            VehicleItem("Helicopter", "🚁", "Chop chop chop spinning in the sky!")
        )
    }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Ready
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun playVehicle(v: VehicleItem) {
        val speech = "${v.name}! ${v.soundDesc}"
        textToSpeech?.speak(speech, TextToSpeech.QUEUE_FLUSH, null, "vehicle_${v.name}")
        coroutineScope.launch {
            progressRepository.recordCompletion("vehicles", 1)
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
                    text = "Vehicles & Transport 🚀",
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

        // Hero feature card for selected vehicle
        val currentVehicle = vehicles[selectedIndex]
        val (cardBg, cardText) = CategoryColors[selectedIndex % CategoryColors.size]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(cardBg)
                .clickable { playVehicle(currentVehicle) }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentVehicle.emoji, fontSize = 72.sp)
                Text(
                    text = currentVehicle.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = cardText,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = currentVehicle.soundDesc,
                    style = MaterialTheme.typography.titleMedium,
                    color = cardText,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VolumeUp,
                        contentDescription = "Sound",
                        tint = cardText,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Listen to vehicle sound!",
                        style = MaterialTheme.typography.labelLarge,
                        color = cardText
                    )
                }
            }
        }

        // Vehicles grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(vehicles) { index, v ->
                val isSelected = index == selectedIndex
                val (itemBg, itemTextColor) = CategoryColors[index % CategoryColors.size]

                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isSelected) ToddlerPrimary else itemBg)
                        .clickable {
                            selectedIndex = index
                            playVehicle(v)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = v.emoji, fontSize = 34.sp)
                        Text(
                            text = v.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else itemTextColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
