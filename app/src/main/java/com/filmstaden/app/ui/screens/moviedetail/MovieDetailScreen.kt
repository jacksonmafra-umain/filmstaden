package com.filmstaden.app.ui.screens.moviedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import com.filmstaden.app.navigation.AppComposeNavigator
import com.filmstaden.app.navigation.SeatSelection
import com.filmstaden.app.ui.components.CinemaContextBar
import com.filmstaden.app.ui.components.DateChip
import com.filmstaden.app.ui.components.FilmstadenButton
import com.filmstaden.app.ui.components.TimeChip
import com.filmstaden.app.ui.theme.BgCard
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailScreen(
    movieId: String,
    navigator: AppComposeNavigator = koinInject(),
    viewModel: MovieDetailViewModel = koinViewModel(parameters = { parametersOf(movieId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movie = state.movie

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            Image(
                painter = painterResource(movie.heroResId ?: movie.posterResId),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            1f to BgDark
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircleIcon(Icons.Filled.ArrowBack, "Back") { navigator.navigateUp() }
                CircleIcon(Icons.Filled.FavoriteBorder, "Favorite") { }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CinemaContextBar(
                city = state.cinema.city,
                cinemaName = state.cinema.name,
                hall = state.cinema.hall,
                onChange = {}
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = movie.title,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (movie.rating > 0f) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FsRed.copy(alpha = 0.18f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = FsRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = movie.rating.toString(),
                            color = FsRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(movie.genre, movie.duration, movie.year, movie.ageRating).forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BgCard)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag,
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = movie.description,
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )

            Text(
                text = "Select date and time",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.dates) { index, date ->
                    DateChip(
                        dayName = date.first,
                        dayNumber = date.second,
                        selected = index == state.selectedDateIndex,
                        onClick = { viewModel.selectDate(index) }
                    )
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.times) { index, time ->
                    TimeChip(
                        time = time,
                        selected = index == state.selectedTimeIndex,
                        onClick = { viewModel.selectTime(index) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            FilmstadenButton(
                text = "Reservation",
                onClick = {
                    navigator.navigate(
                        SeatSelection(
                            movieId = movie.id,
                            date = "${state.selectedDate.first} ${state.selectedDate.second}",
                            time = state.selectedTime
                        )
                    )
                }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CircleIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}
