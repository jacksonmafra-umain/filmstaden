package com.filmstaden.app.ui.screens.seatselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmstaden.app.navigation.AppComposeNavigator
import com.filmstaden.app.ui.components.CinemaContextBar
import com.filmstaden.app.ui.components.FilmstadenButton
import com.filmstaden.app.ui.components.SeatGrid
import com.filmstaden.app.ui.sheets.CinemaSheetViewModel
import com.filmstaden.app.ui.sheets.PaymentSheet
import com.filmstaden.app.ui.theme.BgCard
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.FsRedLight
import com.filmstaden.app.ui.theme.SeatGood
import com.filmstaden.app.ui.theme.SeatReserved
import com.filmstaden.app.ui.theme.SeatSelected
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import com.filmstaden.app.ui.theme.TextSubtle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun SeatSelectionScreen(
    movieId: String,
    date: String,
    time: String,
    navigator: AppComposeNavigator = koinInject(),
    viewModel: SeatSelectionViewModel = koinViewModel(parameters = { parametersOf(movieId, date, time) }),
    cinemaSheetVm: CinemaSheetViewModel = koinInject()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cinema by cinemaSheetVm.selectedCinema.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(top = 48.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(Icons.Filled.ArrowBack, "Back") { navigator.navigateUp() }
            Text(
                text = "Choose Seats",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            CircleIcon(Icons.Filled.CalendarToday, "Calendar") {}
        }

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            CinemaContextBar(
                city = cinema.city,
                cinemaName = cinema.name,
                hall = cinema.hall,
                onChange = cinemaSheetVm::open
            )
        }

        // Ticket counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(state.tierLabel, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(state.priceEachLabel, color = TextMuted, fontSize = 11.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CounterButton(
                    icon = Icons.Filled.Remove,
                    isPrimary = false,
                    enabled = state.canRemoveSeat
                ) { viewModel.removeSeat() }
                Text(
                    text = state.ticketCount.toString(),
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                CounterButton(
                    icon = Icons.Filled.Add,
                    isPrimary = true,
                    enabled = state.canAddSeat
                ) { viewModel.addSeat() }
            }
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenCurve()
            Text(
                "SCREEN",
                color = TextSubtle,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
            SeatGrid(rows = state.rows, onSeatTap = viewModel::toggleSeat)
            Spacer(Modifier.height(8.dp))
            LegendRow()
            Spacer(Modifier.weight(1f))

            // Summary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BgCard)
                    .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.CalendarToday, null, tint = FsRed, modifier = Modifier.size(14.dp))
                    Text("${state.date}  •  ${state.time}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Chair, null, tint = FsRed, modifier = Modifier.size(14.dp))
                    Text("Row E  •  Seats ${state.seatsLabel}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total", color = TextMuted, fontSize = 11.sp)
                    Text(
                        "${state.totalPrice} SEK",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier.width(140.dp)) {
                    FilmstadenButton(
                        text = "Buy",
                        height = 50.dp,
                        onClick = viewModel::openPayment,
                        leading = {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

    }

        PaymentSheet(
            visible = state.isPaymentSheetOpen,
            totalPrice = state.totalPrice,
            onConfirm = {
                viewModel.closePayment()
                navigator.navigate(com.filmstaden.app.navigation.MyTickets)
            },
            onDismiss = viewModel::closePayment
        )
    }
}

@Composable
private fun CircleIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, desc: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(BgCard)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, desc, tint = TextPrimary, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun CounterButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPrimary: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val alpha = if (enabled) 1f else 0.4f
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .then(
                if (isPrimary) Modifier.background(FsRed.copy(alpha = alpha))
                else Modifier.border(BorderStroke(1.5.dp, BorderSubtle.copy(alpha = alpha)), CircleShape)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            null,
            tint = (if (isPrimary) Color.White else TextMuted).copy(alpha = alpha),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun ScreenCurve() {
    Canvas(modifier = Modifier.size(width = 280.dp, height = 24.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height)
            quadraticBezierTo(size.width / 2f, 0f, size.width, size.height)
        }
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(listOf(FsRed, FsRedLight)),
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
private fun LegendRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        LegendItem(SeatGood, "Available")
        LegendItem(SeatReserved, "Reserved")
        LegendItem(SeatSelected, "Selected")
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Text(text, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
