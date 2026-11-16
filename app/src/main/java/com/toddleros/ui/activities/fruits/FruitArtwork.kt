package com.toddleros.ui.activities.fruits

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
fun FruitArtwork(
    fruitId: String,
    size: Dp = 90.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        when (fruitId.lowercase()) {
            "apple" -> drawApple(w, h)
            "banana" -> drawBanana(w, h)
            "orange" -> drawOrange(w, h)
            "mango" -> drawMango(w, h)
            "watermelon" -> drawWatermelon(w, h)
            "grapes" -> drawGrapes(w, h)
            "strawberry" -> drawStrawberry(w, h)
            else -> drawApple(w, h)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawApple(w: Float, h: Float) {
    val red = Color(0xFFEF4444)
    val leaf = Color(0xFF22C55E)
    val stem = Color(0xFF78350F)

    // Stem
    drawOval(stem, topLeft = Offset(w * 0.47f, h * 0.12f), size = Size(w * 0.06f, h * 0.18f))
    // Leaf
    drawOval(leaf, topLeft = Offset(w * 0.52f, h * 0.14f), size = Size(w * 0.22f, h * 0.12f))

    // Apple body (two overlapping circles for heart-like curve)
    drawCircle(red, radius = w * 0.32f, center = Offset(w * 0.38f, h * 0.54f))
    drawCircle(red, radius = w * 0.32f, center = Offset(w * 0.62f, h * 0.54f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.52f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBanana(w: Float, h: Float) {
    val yellow = Color(0xFFFBBF24)
    val stem = Color(0xFF65A30D)

    // Curved banana body
    val path = Path().apply {
        moveTo(w * 0.22f, h * 0.28f)
        cubicTo(w * 0.45f, h * 0.28f, w * 0.78f, h * 0.45f, w * 0.78f, h * 0.78f)
        cubicTo(w * 0.62f, h * 0.85f, w * 0.35f, h * 0.68f, w * 0.22f, h * 0.28f)
        close()
    }
    drawPath(path, yellow)
    // Stem tip
    drawCircle(stem, radius = w * 0.05f, center = Offset(w * 0.22f, h * 0.28f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.54f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawOrange(w: Float, h: Float) {
    val orange = Color(0xFFF97316)
    val leaf = Color(0xFF22C55E)

    // Leaf
    drawOval(leaf, topLeft = Offset(w * 0.5f, h * 0.14f), size = Size(w * 0.24f, h * 0.12f))

    // Round orange body
    drawCircle(orange, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.54f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.52f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMango(w: Float, h: Float) {
    val mango = Color(0xFFF59E0B)
    val leaf = Color(0xFF16A34A)

    // Leaf
    drawOval(leaf, topLeft = Offset(w * 0.52f, h * 0.12f), size = Size(w * 0.24f, h * 0.12f))

    // Mango kidney-like oval
    drawOval(mango, topLeft = Offset(w * 0.22f, h * 0.24f), size = Size(w * 0.56f, h * 0.64f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.54f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWatermelon(w: Float, h: Float) {
    val rind = Color(0xFF15803D)
    val flesh = Color(0xFFF43F5E)
    val seed = Color(0xFF1E293B)

    // Rind slice (half circle)
    drawArc(
        color = rind,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.15f, h * 0.25f),
        size = Size(w * 0.7f, h * 0.65f)
    )

    // Red flesh inside
    drawArc(
        color = flesh,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.19f, h * 0.27f),
        size = Size(w * 0.62f, h * 0.57f)
    )

    // Seeds
    drawCircle(seed, radius = w * 0.02f, center = Offset(w * 0.38f, h * 0.44f))
    drawCircle(seed, radius = w * 0.02f, center = Offset(w * 0.62f, h * 0.44f))
    drawCircle(seed, radius = w * 0.02f, center = Offset(w * 0.5f, h * 0.48f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.36f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGrapes(w: Float, h: Float) {
    val purple = Color(0xFF9333EA)
    val leaf = Color(0xFF22C55E)

    // Top leaf
    drawOval(leaf, topLeft = Offset(w * 0.4f, h * 0.16f), size = Size(w * 0.24f, h * 0.14f))

    // Cluster of grape circles
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.36f, h * 0.38f))
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.64f, h * 0.38f))
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.44f))
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.35f, h * 0.56f))
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.65f, h * 0.56f))
    drawCircle(purple, radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.7f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.5f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStrawberry(w: Float, h: Float) {
    val red = Color(0xFFE11D48)
    val crown = Color(0xFF22C55E)
    val seed = Color(0xFFFDE047)

    // Green leafy crown
    drawOval(crown, topLeft = Offset(w * 0.35f, h * 0.16f), size = Size(w * 0.3f, h * 0.14f))

    // Heart-like strawberry body
    val body = Path().apply {
        moveTo(w * 0.22f, h * 0.32f)
        lineTo(w * 0.78f, h * 0.32f)
        quadraticTo(w * 0.82f, h * 0.65f, w * 0.5f, h * 0.85f)
        quadraticTo(w * 0.18f, h * 0.65f, w * 0.22f, h * 0.32f)
        close()
    }
    drawPath(body, red)

    // Yellow seeds
    drawCircle(seed, radius = w * 0.015f, center = Offset(w * 0.32f, h * 0.42f))
    drawCircle(seed, radius = w * 0.015f, center = Offset(w * 0.68f, h * 0.42f))
    drawCircle(seed, radius = w * 0.015f, center = Offset(w * 0.5f, h * 0.72f))

    // Eyes & Smile
    drawFruitFace(w, h, centerY = h * 0.5f)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFruitFace(w: Float, h: Float, centerY: Float) {
    val eyeColor = Color(0xFF1E293B)
    val blush = Color(0xFFFCA5A5).copy(alpha = 0.7f)

    // Blinking / Smiling eyes
    drawCircle(eyeColor, radius = w * 0.035f, center = Offset(w * 0.42f, centerY))
    drawCircle(eyeColor, radius = w * 0.035f, center = Offset(w * 0.58f, centerY))
    drawCircle(Color.White, radius = w * 0.012f, center = Offset(w * 0.43f, centerY - 2f))
    drawCircle(Color.White, radius = w * 0.012f, center = Offset(w * 0.59f, centerY - 2f))

    // Cute blush cheeks
    drawCircle(blush, radius = w * 0.045f, center = Offset(w * 0.34f, centerY + 8f))
    drawCircle(blush, radius = w * 0.045f, center = Offset(w * 0.66f, centerY + 8f))

    // Smile
    drawOval(Color(0xFFE11D48), topLeft = Offset(w * 0.47f, centerY + 8f), size = Size(w * 0.06f, h * 0.04f))
}
