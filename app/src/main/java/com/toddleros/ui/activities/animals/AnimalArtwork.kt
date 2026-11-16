package com.toddleros.ui.activities.animals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimalArtwork(
    animalId: String,
    size: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        when (animalId.lowercase()) {
            "dog" -> drawDog(w, h)
            "cat" -> drawCat(w, h)
            "cow" -> drawCow(w, h)
            "lion" -> drawLion(w, h)
            "duck" -> drawDuck(w, h)
            "elephant" -> drawElephant(w, h)
            "sheep" -> drawSheep(w, h)
            "monkey" -> drawMonkey(w, h)
            else -> drawDog(w, h)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDog(w: Float, h: Float) {
    val brown = Color(0xFFD97706)
    val darkBrown = Color(0xFF92400E)
    val eyeColor = Color(0xFF1E293B)
    val tongueColor = Color(0xFFF43F5E)

    // Ears
    drawOval(color = darkBrown, topLeft = Offset(w * 0.1f, h * 0.2f), size = Size(w * 0.25f, h * 0.45f))
    drawOval(color = darkBrown, topLeft = Offset(w * 0.65f, h * 0.2f), size = Size(w * 0.25f, h * 0.45f))

    // Head
    drawCircle(color = brown, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.45f))

    // Snout
    drawOval(color = Color(0xFFFEF3C7), topLeft = Offset(w * 0.35f, h * 0.42f), size = Size(w * 0.3f, h * 0.26f))
    drawOval(color = eyeColor, topLeft = Offset(w * 0.43f, h * 0.44f), size = Size(w * 0.14f, h * 0.1f))

    // Tongue
    drawOval(color = tongueColor, topLeft = Offset(w * 0.46f, h * 0.58f), size = Size(w * 0.08f, h * 0.12f))

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.38f, h * 0.36f))
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.36f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.39f, h * 0.35f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.63f, h * 0.35f))

    // Cheeks
    drawCircle(color = Color(0xFFFCA5A5).copy(alpha = 0.6f), radius = w * 0.05f, center = Offset(w * 0.28f, h * 0.48f))
    drawCircle(color = Color(0xFFFCA5A5).copy(alpha = 0.6f), radius = w * 0.05f, center = Offset(w * 0.72f, h * 0.48f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCat(w: Float, h: Float) {
    val catOrange = Color(0xFFFB923C)
    val innerEar = Color(0xFFFDE047)
    val eyeColor = Color(0xFF1E293B)

    // Triangular ears
    val leftEar = Path().apply {
        moveTo(w * 0.22f, h * 0.4f)
        lineTo(w * 0.25f, h * 0.12f)
        lineTo(w * 0.44f, h * 0.26f)
        close()
    }
    val rightEar = Path().apply {
        moveTo(w * 0.78f, h * 0.4f)
        lineTo(w * 0.75f, h * 0.12f)
        lineTo(w * 0.56f, h * 0.26f)
        close()
    }
    drawPath(leftEar, catOrange)
    drawPath(rightEar, catOrange)

    // Inner ear accents
    drawCircle(innerEar, radius = w * 0.06f, center = Offset(w * 0.31f, h * 0.26f))
    drawCircle(innerEar, radius = w * 0.06f, center = Offset(w * 0.69f, h * 0.26f))

    // Head
    drawCircle(color = catOrange, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f))

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.38f, h * 0.45f))
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.45f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.39f, h * 0.44f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.63f, h * 0.44f))

    // Cute pink nose
    drawOval(color = Color(0xFFF43F5E), topLeft = Offset(w * 0.46f, h * 0.53f), size = Size(w * 0.08f, h * 0.06f))

    // Cheeks
    drawCircle(color = Color(0xFFFCA5A5).copy(alpha = 0.6f), radius = w * 0.05f, center = Offset(w * 0.28f, h * 0.55f))
    drawCircle(color = Color(0xFFFCA5A5).copy(alpha = 0.6f), radius = w * 0.05f, center = Offset(w * 0.72f, h * 0.55f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCow(w: Float, h: Float) {
    val cowWhite = Color(0xFFF8FAFC)
    val spotBlack = Color(0xFF334155)
    val snoutPink = Color(0xFFFBCFE8)
    val hornColor = Color(0xFFFDE68A)

    // Horns
    drawOval(hornColor, topLeft = Offset(w * 0.28f, h * 0.15f), size = Size(w * 0.1f, h * 0.18f))
    drawOval(hornColor, topLeft = Offset(w * 0.62f, h * 0.15f), size = Size(w * 0.1f, h * 0.18f))

    // Ears
    drawOval(cowWhite, topLeft = Offset(w * 0.1f, h * 0.3f), size = Size(w * 0.22f, h * 0.15f))
    drawOval(cowWhite, topLeft = Offset(w * 0.68f, h * 0.3f), size = Size(w * 0.22f, h * 0.15f))

    // Head
    drawCircle(color = cowWhite, radius = w * 0.33f, center = Offset(w * 0.5f, h * 0.45f))
    // Black patch spot
    drawCircle(color = spotBlack, radius = w * 0.14f, center = Offset(w * 0.38f, h * 0.35f))

    // Snout
    drawOval(color = snoutPink, topLeft = Offset(w * 0.3f, h * 0.5f), size = Size(w * 0.4f, h * 0.26f))
    // Nostrils
    drawCircle(color = Color(0xFF9D174D), radius = w * 0.03f, center = Offset(w * 0.42f, h * 0.63f))
    drawCircle(color = Color(0xFF9D174D), radius = w * 0.03f, center = Offset(w * 0.58f, h * 0.63f))

    // Eyes
    drawCircle(color = spotBlack, radius = w * 0.04f, center = Offset(w * 0.37f, h * 0.37f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.38f, h * 0.36f))
    drawCircle(color = spotBlack, radius = w * 0.04f, center = Offset(w * 0.63f, h * 0.37f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.64f, h * 0.36f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLion(w: Float, h: Float) {
    val maneBrown = Color(0xFFD97706)
    val lionFace = Color(0xFFFDE68A)
    val eyeColor = Color(0xFF1E293B)

    // Big fluffy mane
    drawCircle(color = maneBrown, radius = w * 0.42f, center = Offset(w * 0.5f, h * 0.5f))

    // Round ears
    drawCircle(color = lionFace, radius = w * 0.1f, center = Offset(w * 0.28f, h * 0.22f))
    drawCircle(color = lionFace, radius = w * 0.1f, center = Offset(w * 0.72f, h * 0.22f))

    // Face
    drawCircle(color = lionFace, radius = w * 0.3f, center = Offset(w * 0.5f, h * 0.5f))

    // Muzzle & Nose
    drawCircle(color = Color(0xFFFEF3C7), radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.58f))
    drawOval(color = Color(0xFF78350F), topLeft = Offset(w * 0.45f, h * 0.52f), size = Size(w * 0.1f, h * 0.07f))

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.39f, h * 0.44f))
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.61f, h * 0.44f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.4f, h * 0.43f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.62f, h * 0.43f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDuck(w: Float, h: Float) {
    val yellow = Color(0xFFFACC15)
    val beakOrange = Color(0xFFEA580C)
    val eyeColor = Color(0xFF1E293B)

    // Duck head
    drawCircle(color = yellow, radius = w * 0.34f, center = Offset(w * 0.5f, h * 0.45f))

    // Wide happy beak
    drawOval(color = beakOrange, topLeft = Offset(w * 0.32f, h * 0.5f), size = Size(w * 0.36f, h * 0.22f))

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.38f, h * 0.38f))
    drawCircle(color = eyeColor, radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.38f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.39f, h * 0.37f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.63f, h * 0.37f))

    // Rosy cheeks
    drawCircle(color = Color(0xFFFB923C).copy(alpha = 0.5f), radius = w * 0.05f, center = Offset(w * 0.25f, h * 0.48f))
    drawCircle(color = Color(0xFFFB923C).copy(alpha = 0.5f), radius = w * 0.05f, center = Offset(w * 0.75f, h * 0.48f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawElephant(w: Float, h: Float) {
    val grey = Color(0xFF94A3B8)
    val innerEar = Color(0xFFFBCFE8)
    val eyeColor = Color(0xFF1E293B)

    // Big floppy ears
    drawCircle(color = grey, radius = w * 0.26f, center = Offset(w * 0.2f, h * 0.42f))
    drawCircle(color = innerEar, radius = w * 0.17f, center = Offset(w * 0.2f, h * 0.42f))
    drawCircle(color = grey, radius = w * 0.26f, center = Offset(w * 0.8f, h * 0.42f))
    drawCircle(color = innerEar, radius = w * 0.17f, center = Offset(w * 0.8f, h * 0.42f))

    // Head
    drawCircle(color = grey, radius = w * 0.33f, center = Offset(w * 0.5f, h * 0.45f))

    // Trunk
    val trunk = Path().apply {
        moveTo(w * 0.44f, h * 0.48f)
        lineTo(w * 0.42f, h * 0.75f)
        quadraticTo(w * 0.42f, h * 0.88f, w * 0.58f, h * 0.84f)
        lineTo(w * 0.56f, h * 0.76f)
        quadraticTo(w * 0.5f, h * 0.78f, w * 0.5f, h * 0.68f)
        lineTo(w * 0.56f, h * 0.48f)
        close()
    }
    drawPath(trunk, grey)

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.38f, h * 0.38f))
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.62f, h * 0.38f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.39f, h * 0.37f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.63f, h * 0.37f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSheep(w: Float, h: Float) {
    val woolWhite = Color(0xFFF8FAFC)
    val faceDark = Color(0xFF475569)
    val eyeColor = Color(0xFF1E293B)

    // Cloud-like wool background
    drawCircle(woolWhite, radius = w * 0.18f, center = Offset(w * 0.3f, h * 0.28f))
    drawCircle(woolWhite, radius = w * 0.18f, center = Offset(w * 0.7f, h * 0.28f))
    drawCircle(woolWhite, radius = w * 0.2f, center = Offset(w * 0.5f, h * 0.22f))
    drawCircle(woolWhite, radius = w * 0.22f, center = Offset(w * 0.25f, h * 0.55f))
    drawCircle(woolWhite, radius = w * 0.22f, center = Offset(w * 0.75f, h * 0.55f))

    // Ears
    drawOval(faceDark, topLeft = Offset(w * 0.15f, h * 0.42f), size = Size(w * 0.18f, h * 0.1f))
    drawOval(faceDark, topLeft = Offset(w * 0.67f, h * 0.42f), size = Size(w * 0.18f, h * 0.1f))

    // Dark cute face
    drawOval(color = faceDark, topLeft = Offset(w * 0.33f, h * 0.35f), size = Size(w * 0.34f, h * 0.42f))

    // Eyes
    drawCircle(color = Color.White, radius = w * 0.045f, center = Offset(w * 0.42f, h * 0.48f))
    drawCircle(color = eyeColor, radius = w * 0.025f, center = Offset(w * 0.42f, h * 0.48f))
    drawCircle(color = Color.White, radius = w * 0.045f, center = Offset(w * 0.58f, h * 0.48f))
    drawCircle(color = eyeColor, radius = w * 0.025f, center = Offset(w * 0.58f, h * 0.48f))

    // Rosy pink nose
    drawOval(color = Color(0xFFF472B6), topLeft = Offset(w * 0.46f, h * 0.62f), size = Size(w * 0.08f, h * 0.05f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMonkey(w: Float, h: Float) {
    val brown = Color(0xFF854D0E)
    val peach = Color(0xFFFED7AA)
    val eyeColor = Color(0xFF1E293B)

    // Big round ears
    drawCircle(color = brown, radius = w * 0.16f, center = Offset(w * 0.18f, h * 0.42f))
    drawCircle(color = peach, radius = w * 0.1f, center = Offset(w * 0.18f, h * 0.42f))
    drawCircle(color = brown, radius = w * 0.16f, center = Offset(w * 0.82f, h * 0.42f))
    drawCircle(color = peach, radius = w * 0.1f, center = Offset(w * 0.82f, h * 0.42f))

    // Head
    drawCircle(color = brown, radius = w * 0.34f, center = Offset(w * 0.5f, h * 0.45f))

    // Peach heart-shaped face
    drawCircle(color = peach, radius = w * 0.16f, center = Offset(w * 0.42f, h * 0.42f))
    drawCircle(color = peach, radius = w * 0.16f, center = Offset(w * 0.58f, h * 0.42f))
    drawOval(color = peach, topLeft = Offset(w * 0.32f, h * 0.46f), size = Size(w * 0.36f, h * 0.24f))

    // Smile & Nose
    drawCircle(color = Color(0xFF78350F), radius = w * 0.02f, center = Offset(w * 0.46f, h * 0.53f))
    drawCircle(color = Color(0xFF78350F), radius = w * 0.02f, center = Offset(w * 0.54f, h * 0.53f))
    drawOval(color = Color(0xFFDC2626), topLeft = Offset(w * 0.44f, h * 0.59f), size = Size(w * 0.12f, h * 0.06f))

    // Eyes
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.41f, h * 0.39f))
    drawCircle(color = eyeColor, radius = w * 0.04f, center = Offset(w * 0.59f, h * 0.39f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.42f, h * 0.38f))
    drawCircle(color = Color.White, radius = w * 0.015f, center = Offset(w * 0.6f, h * 0.38f))
}
