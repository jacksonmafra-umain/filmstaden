package com.filmstaden.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filmstaden.app.ui.theme.BgCardLight
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary

@Composable
fun DateChip(
    dayName: String,
    dayNumber: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg by animateColorAsState(
        targetValue = if (selected) FsRed else BgCardLight,
        animationSpec = tween(220),
        label = "dateChipBg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) FsRed else BorderSubtle,
        animationSpec = tween(220),
        label = "dateChipBorder"
    )
    val textColor = if (selected) TextPrimary else TextPrimary
    val dayColor = if (selected) TextPrimary.copy(alpha = 0.9f) else TextMuted

    Column(
        modifier = modifier
            .width(56.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = dayName,
            color = dayColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = dayNumber.toString(),
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
