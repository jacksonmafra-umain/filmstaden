package com.filmstaden.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Decorative QR code — deterministic pseudo-random grid derived from [data].
 * Purely visual (not a real QR encoding) but looks authentic.
 */
@Composable
fun QrCodeView(
    data: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    cells: Int = 21
) {
    val pattern = remember(data, cells) {
        val rand = Random(data.hashCode().toLong())
        Array(cells) { BooleanArray(cells) { rand.nextFloat() > 0.5f } }.also { grid ->
            // Add 3 position markers (top-left, top-right, bottom-left)
            fun drawMarker(row: Int, col: Int) {
                for (r in row until row + 7) {
                    for (c in col until col + 7) {
                        val onEdge = r == row || r == row + 6 || c == col || c == col + 6
                        val innerBox = r in (row + 2)..(row + 4) && c in (col + 2)..(col + 4)
                        grid[r][c] = onEdge || innerBox
                    }
                }
            }
            drawMarker(0, 0)
            drawMarker(0, cells - 7)
            drawMarker(cells - 7, 0)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.size(size - 16.dp)) {
            val cellSize = this.size.width / cells
            for (r in 0 until cells) {
                for (c in 0 until cells) {
                    if (pattern[r][c]) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
    }
}
