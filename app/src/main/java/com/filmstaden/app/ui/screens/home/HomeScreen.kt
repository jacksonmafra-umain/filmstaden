package com.filmstaden.app.ui.screens.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.filmstaden.app.data.models.Movie
import com.filmstaden.app.data.models.MovieCategory
import com.filmstaden.app.navigation.AppComposeNavigator
import com.filmstaden.app.navigation.MovieDetail
import com.filmstaden.app.ui.components.FilmstadenHeader
import com.filmstaden.app.ui.components.MoviePosterCard
import com.filmstaden.app.ui.sheets.CinemaSheetViewModel
import com.filmstaden.app.ui.theme.BgDark
import com.filmstaden.app.ui.theme.BorderSubtle
import com.filmstaden.app.ui.theme.FsRed
import com.filmstaden.app.ui.theme.TextMuted
import com.filmstaden.app.ui.theme.TextPrimary
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    navigator: AppComposeNavigator = koinInject(),
    viewModel: HomeViewModel = koinViewModel(),
    cinemaSheetVm: CinemaSheetViewModel = koinInject()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedCinema by cinemaSheetVm.selectedCinema.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            FilmstadenHeader {
                CinemaLocationRow(
                    cinemaName = selectedCinema.name,
                    onChange = cinemaSheetVm::open
                )
                Text(
                    text = "Hey ${state.userName}, Good Afternoon!",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                SearchBar()
            }

            Spacer(Modifier.height(20.dp))

            MovieSection(
                title = "Now Playing",
                movies = state.nowPlaying,
                onMovieClick = { navigator.navigate(MovieDetail(it.id)) }
            )
            Spacer(Modifier.height(24.dp))
            MovieSection(
                title = "Coming Soon",
                movies = state.comingSoon,
                onMovieClick = { navigator.navigate(MovieDetail(it.id)) }
            )
            Spacer(Modifier.height(24.dp))
            MovieSection(
                title = "Top Movies",
                movies = state.topMovies,
                onMovieClick = { navigator.navigate(MovieDetail(it.id)) }
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CinemaLocationRow(cinemaName: String, onChange: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Filled.Place,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "CINEMA AT",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        Text(
            text = cinemaName,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .clickable(onClick = onChange)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Change",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "Search movies, cinemas...",
            color = TextMuted,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MovieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See all",
                color = FsRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(movies) { _, movie ->
                MoviePosterCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    modifier = Modifier.size(width = 108.dp, height = 160.dp)
                )
            }
        }
    }
}
