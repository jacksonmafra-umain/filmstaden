package com.filmstaden.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filmstaden.app.data.models.Seat
import com.filmstaden.app.data.models.SeatStatus
import com.filmstaden.app.data.models.SeatTier
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.SeatBetter
import com.filmstaden.app.ui.theme.SeatGood
import com.filmstaden.app.ui.theme.SeatReserved
import com.filmstaden.app.ui.theme.SeatSelected
import com.filmstaden.app.ui.theme.TextSubtle

@Composable
fun SeatGrid(
    rows: List<List<Seat>>,
    onSeatTap: (Seat) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        rows.forEach { rowSeats ->
            val label = rowSeats.firstOrNull()?.row ?: ' '
            val half = rowSeats.size / 2
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowSeats.take(half).forEach { seat ->
                    SeatCell(seat = seat, onTap = { onSeatTap(seat) })
                }
                Box(
                    modifier = Modifier.size(width = 18.dp, height = 26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label.toString(),
                        color = TextSubtle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                rowSeats.drop(half).forEach { seat ->
                    SeatCell(seat = seat, onTap = { onSeatTap(seat) })
                }
            }
        }
    }
}

@Composable
private fun SeatCell(seat: Seat, onTap: () -> Unit) {
    val color = when (seat.status) {
        SeatStatus.RESERVED -> SeatReserved
        SeatStatus.SELECTED -> SeatSelected
        SeatStatus.AVAILABLE -> when (seat.tier) {
            SeatTier.GOOD -> SeatGood
            SeatTier.BETTER -> SeatBetter
            SeatTier.BEST -> com.filmstaden.app.ui.theme.SeatBest
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (seat.status == SeatStatus.SELECTED) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "seatScale"
    )

    Box(
        modifier = Modifier
            .size(26.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .clickable(
                enabled = seat.status != SeatStatus.RESERVED,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onTap() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = seat.status == SeatStatus.SELECTED,
            enter = scaleIn(spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = BgDark,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

