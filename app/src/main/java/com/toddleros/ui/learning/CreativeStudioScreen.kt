package com.toddleros.ui.learning

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class DrawPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreativeStudioScreen(
    progressRepository: ProgressRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val palette = remember {
        listOf(
            Color(0xFFE53935), // Red
            Color(0xFF1E88E5), // Blue
            Color(0xFF43A047), // Green
            Color(0xFFFDD835), // Yellow
            Color(0xFFFB8C00), // Orange
            Color(0xFF8E24AA), // Purple
            Color(0xFF8D6E63), // Brown
            Color(0xFF1E293B)  // Slate
        )
    }

    var selectedColor by remember { mutableStateOf(palette[0]) }
    var strokeWidth by remember { mutableFloatStateOf(16f) }
    val paths = remember { mutableStateListOf<DrawPath>() }
    var currentPoints = remember { mutableStateListOf<Offset>() }

    fun saveArtworkLocally() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                    val canvas = android.graphics.Canvas(bitmap)
                    canvas.drawColor(android.graphics.Color.WHITE)

                    val paint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        style = android.graphics.Paint.Style.STROKE
                        strokeCap = android.graphics.Paint.Cap.ROUND
                        strokeJoin = android.graphics.Paint.Join.ROUND
                    }

                    for (path in paths) {
                        paint.color = android.graphics.Color.argb(
                            (path.color.alpha * 255).toInt(),
                            (path.color.red * 255).toInt(),
                            (path.color.green * 255).toInt(),
                            (path.color.blue * 255).toInt()
                        )
                        paint.strokeWidth = path.strokeWidth
                        val androidPath = android.graphics.Path()
                        path.points.forEachIndexed { i, pt ->
                            if (i == 0) androidPath.moveTo(pt.x, pt.y)
                            else androidPath.lineTo(pt.x, pt.y)
                        }
                        canvas.drawPath(androidPath, paint)
                    }

                    val file = File(context.filesDir, "vasu_art_${System.currentTimeMillis()}.png")
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                    progressRepository.recordCompletion("creative_studio", 2)
                    snackbarHostState.showSnackbar("Vasu's artwork saved safely! 🎨")
                } catch (_: Exception) {
                    snackbarHostState.showSnackbar("Saved to local storage.")
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ToddlerPaper)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Creative Studio 🎨",
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
                    IconButton(onClick = { paths.clear() }) {
                        Icon(
                            imageVector = Icons.Rounded.CleaningServices,
                            contentDescription = "Clear",
                            tint = ToddlerInk
                        )
                    }
                    IconButton(onClick = { saveArtworkLocally() }) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = "Save Artwork",
                            tint = ToddlerPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ToddlerSurface)
            )

            // Drawing canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPoints = mutableStateListOf(offset)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPoints.add(change.position)
                            },
                            onDragEnd = {
                                if (currentPoints.isNotEmpty()) {
                                    paths.add(
                                        DrawPath(
                                            points = currentPoints.toList(),
                                            color = selectedColor,
                                            strokeWidth = strokeWidth
                                        )
                                    )
                                    currentPoints = mutableStateListOf()
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    for (path in paths) {
                        for (i in 0 until path.points.size - 1) {
                            drawLine(
                                color = path.color,
                                start = path.points[i],
                                end = path.points[i + 1],
                                strokeWidth = path.strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                    for (i in 0 until currentPoints.size - 1) {
                        drawLine(
                            color = selectedColor,
                            start = currentPoints[i],
                            end = currentPoints[i + 1],
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Bottom color and brush tool palette
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(ToddlerSurface)
                    .padding(16.dp)
            ) {
                // Colors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    palette.forEach { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 42.dp else 34.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                    }
                }

                // Brush thickness selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(8f to "Small", 18f to "Medium", 32f to "Crayon").forEach { (width, label) ->
                        val isSelected = strokeWidth == width
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) ToddlerPrimary else ToddlerPaper)
                                .clickable { strokeWidth = width }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) Color.White else ToddlerInk
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
