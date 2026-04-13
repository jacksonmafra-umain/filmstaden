package com.filmstaden.app.ui.screens.tickets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmstaden.app.data.models.Ticket
import com.filmstaden.app.ui.components.CinemaContextBar
import com.filmstaden.app.ui.components.QrCodeView
import com.filmstaden.app.ui.theme.BgCard
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import com.filmstaden.app.ui.theme.TextSubtle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyTicketsScreen(
    viewModel: MyTicketsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState { state.tickets.size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(top = 48.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("My Tickets", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (state.tickets.isNotEmpty()) {
            val ticket = state.tickets.first()
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                CinemaContextBar(
                    city = ticket.cinema.city,
                    cinemaName = ticket.cinema.name,
                    hall = ticket.cinema.hall,
                    onChange = null
                )
            }
        }

        Text(
            "Scan each QR code at the entrance. You have ${state.tickets.size} tickets.",
            color = TextMuted,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 28.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            TicketCard(state.tickets[page])
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
        ) {
            state.tickets.indices.forEach { i ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (i == pagerState.currentPage) FsRed else TextSubtle.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun TicketCard(ticket: Ticket) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(BgCard)
            .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Image(
                painter = painterResource(com.filmstaden.app.R.drawable.ticket_hail_mary),
                contentDescription = ticket.movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.3f to Color.Transparent,
                            1f to BgCard
                        )
                    )
            )
            Text(
                text = ticket.movie.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 12.dp)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ticket ${ticket.ticketNumber} of ${ticket.totalTickets}", color = FsRed, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FsRed.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Seat ${ticket.seatNumber}", color = FsRed, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValue("Date:", ticket.date)
                LabelValue("Time:", ticket.time)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValue("Row:", ticket.row)
                LabelValue("Seats:", ticket.seatNumber)
            }
            Text("${ticket.cinema.name}  •  ${ticket.cinema.hall}", color = TextMuted, fontSize = 11.sp)

            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(TextSubtle.copy(alpha = 0.3f))
            )
            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                QrCodeView(data = ticket.qrData, size = 130.dp)
            }
        }
    }
}

@Composable
private fun LabelValue(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = FsRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
