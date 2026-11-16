package com.toddleros.ui.content.stories

import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.toddleros.core.theme.PastelLavender
import com.toddleros.core.theme.PastelLavenderDark
import com.toddleros.core.theme.ToddlerInk
import com.toddleros.core.theme.ToddlerPaper
import com.toddleros.core.theme.ToddlerPrimary
import com.toddleros.core.theme.ToddlerSurface
import com.toddleros.domain.model.StoryBook
import java.util.Locale

@Composable
fun StoryReaderScreen(
    story: StoryBook,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentPageIndex by remember { mutableIntStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }

    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
        tts.language = Locale.getDefault()
        textToSpeech = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val page = story.pages.getOrNull(currentPageIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToddlerPaper)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close Story",
                    tint = ToddlerInk
                )
            }

            Text(
                text = "${currentPageIndex + 1} / ${story.pages.size.coerceAtLeast(1)}",
                style = MaterialTheme.typography.titleMedium,
                color = ToddlerInk
            )

            if (ttsReady && page != null) {
                IconButton(
                    onClick = {
                        textToSpeech?.speak(
                            page.text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "story_page"
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = "Read Aloud",
                        tint = ToddlerPrimary
                    )
                }
            } else {
                Box(modifier = Modifier.padding(24.dp))
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(ToddlerSurface)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (page?.imageUri != null) {
                    AsyncImage(
                        model = Uri.parse(page.imageUri),
                        contentDescription = "Illustration",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Text(text = "📖", fontSize = 64.sp)
                }

                Text(
                    text = page?.text ?: story.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = ToddlerInk,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentPageIndex > 0) currentPageIndex-- },
                enabled = currentPageIndex > 0,
                colors = ButtonDefaults.buttonColors(containerColor = PastelLavender),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back Page",
                    tint = PastelLavenderDark
                )
                Text(
                    text = " Previous",
                    style = MaterialTheme.typography.labelLarge,
                    color = PastelLavenderDark
                )
            }

            Button(
                onClick = { if (currentPageIndex < story.pages.size - 1) currentPageIndex++ },
                enabled = currentPageIndex < story.pages.size - 1,
                colors = ButtonDefaults.buttonColors(containerColor = PastelLavender),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Next ",
                    style = MaterialTheme.typography.labelLarge,
                    color = PastelLavenderDark
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Next Page",
                    tint = PastelLavenderDark
                )
            }
        }
    }
}
