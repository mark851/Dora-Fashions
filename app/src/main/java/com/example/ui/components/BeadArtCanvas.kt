package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private val PaletteMap = mapOf(
    "Gold" to Color(0xFFD4AF37),
    "Silver" to Color(0xFFC0C4C8),
    "Rose Gold" to Color(0xFFB76E79),
    "Red" to Color(0xFFC0392B),
    "Blue" to Color(0xFF2E6FBF),
    "Green" to Color(0xFF2E8B57),
    "Pink" to Color(0xFFE88FB0),
    "White" to Color(0xFFFBFBF8),
    "Black" to Color(0xFF2A2A2A),
    "Purple" to Color(0xFF7D4FB3),
    "Clear" to Color(0xFFD4EFFC),
    "Pearl" to Color(0xFFF3ECE0),
    "Brown" to Color(0xFF8A5A3B),
    "Terracotta" to Color(0xFFD2691E),
    "Orange" to Color(0xFFEE8A2B),
    "Amber" to Color(0xFFFFBF00),
    "Emerald" to Color(0xFF50C878)
)

private val DefaultRainbow = listOf(
    Color(0xFFD4AF37),
    Color(0xFFC0392B),
    Color(0xFF2E6FBF),
    Color(0xFF2E8B57),
    Color(0xFFE88FB0),
    Color(0xFF7D4FB3),
    Color(0xFFEE8A2B),
    Color(0xFFF3ECE0)
)

@Composable
fun BeadArtCanvas(
    seed: Long,
    colors: List<String>,
    modifier: Modifier = Modifier
) {
    val activeColors = colors.mapNotNull { PaletteMap[it] }.ifEmpty { DefaultRainbow }

    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(Color(0xFFF8F2E6), Color(0xFFE7D8C0))
            )
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val rng = Random(seed)

            // Draw a subtle connecting wire or strand curve
            val beadCount = 18
            for (i in 0 until beadCount) {
                val radius = (w / 12f) + rng.nextFloat() * (w / 10f)
                val cx = rng.nextFloat() * (w * 0.85f) + w * 0.08f
                val cy = rng.nextFloat() * (h * 0.85f) + h * 0.08f
                val beadColor = activeColors[i % activeColors.size]

                // Bead drop shadow
                drawCircle(
                    color = Color(0x33000000),
                    radius = radius * 1.05f,
                    center = Offset(cx + 4f, cy + 5f)
                )

                // Base bead body
                drawCircle(
                    color = beadColor,
                    radius = radius,
                    center = Offset(cx, cy)
                )

                // Radial shine/highlight (giving glassy/pearly 3D bead sheen)
                val highlightCenter = Offset(cx - radius * 0.32f, cy - radius * 0.35f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.85f),
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = highlightCenter,
                        radius = radius * 0.7f
                    ),
                    radius = radius * 0.7f,
                    center = highlightCenter
                )

                // Inner bead hole / reflection dot
                drawCircle(
                    color = Color.Black.copy(alpha = 0.15f),
                    radius = radius * 0.18f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}
