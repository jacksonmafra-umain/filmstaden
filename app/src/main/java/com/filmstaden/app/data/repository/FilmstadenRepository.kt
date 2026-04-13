package com.filmstaden.app.data.repository

import com.filmstaden.app.R
import com.filmstaden.app.data.models.Cinema
import com.filmstaden.app.data.models.Movie
import com.filmstaden.app.data.models.MovieCategory
import com.filmstaden.app.data.models.Seat
import com.filmstaden.app.data.models.SeatStatus
import com.filmstaden.app.data.models.SeatTier
import com.filmstaden.app.data.models.Ticket
import com.filmstaden.app.data.models.UserProfile

class FilmstadenRepository {

    private val hailMary = Movie(
        id = "hail-mary",
        title = "Project Hail Mary",
        posterResId = R.drawable.poster_00,
        heroResId = R.drawable.hero_hail_mary,
        genre = "Sci-Fi",
        duration = "2h 17min",
        year = "2025",
        ageRating = "13+",
        rating = 8.5f,
        description = "A lone astronaut must save Earth from an extinction-level threat. Stranded light-years from home on a desperate mission, he discovers an unexpected ally... read more",
        category = MovieCategory.NOW_PLAYING
    )

    private val movies: List<Movie> = listOf(
        hailMary,
        Movie("stardust-abyss", "Stardust Abyss", R.drawable.poster_01, null, "Sci-Fi", "1h 58min", "2025", "11+", 7.9f, "A thrilling journey through a cosmic anomaly.", MovieCategory.NOW_PLAYING),
        Movie("nights-shadow", "Night's Shadow", R.drawable.poster_02, null, "Thriller", "2h 02min", "2025", "15+", 7.4f, "A mysterious woman uncovers truths in the neon-lit streets.", MovieCategory.NOW_PLAYING),
        Movie("fire-and-ashes", "Fire and Ashes", R.drawable.poster_03, null, "Fantasy", "2h 23min", "2025", "13+", 8.1f, "An epic dragon battle across kingdoms.", MovieCategory.NOW_PLAYING),
        Movie("redline-sunset", "Redline Sunset", R.drawable.poster_04, null, "Action", "1h 51min", "2025", "13+", 7.6f, "A hero's last stand under a crimson sky.", MovieCategory.NOW_PLAYING),

        Movie("abyssal-awakening", "Abyssal Awakening", R.drawable.poster_05, null, "Sci-Fi", "2h 15min", "2026", "13+", 0f, "Deep ocean mysteries rise to the surface.", MovieCategory.COMING_SOON),
        Movie("the-last-rain", "The Last Rain", R.drawable.poster_06, null, "Drama", "1h 47min", "2026", "11+", 0f, "A samurai's journey through a storm.", MovieCategory.COMING_SOON),
        Movie("burn-night-circuit", "Burn: Night Circuit", R.drawable.poster_07, null, "Action", "1h 54min", "2026", "15+", 0f, "Underground racing at full throttle.", MovieCategory.COMING_SOON),
        Movie("haunting", "Haunting", R.drawable.poster_08, null, "Horror", "1h 42min", "2026", "15+", 0f, "A haunted manor holds its secrets.", MovieCategory.COMING_SOON),

        Movie("northman", "Northman", R.drawable.poster_09, null, "Action", "2h 17min", "2024", "15+", 8.2f, "A Viking warrior's path of vengeance.", MovieCategory.TOP_MOVIES),
        Movie("steel-soul", "Steel & Soul", R.drawable.poster_10, null, "Sci-Fi", "2h 04min", "2024", "11+", 8.0f, "A robot and child navigate a futuristic city.", MovieCategory.TOP_MOVIES),
        Movie("shadow-city", "Shadow City", R.drawable.poster_11, null, "Mystery", "1h 55min", "2024", "13+", 7.8f, "A detective chases ghosts in a rainy metropolis.", MovieCategory.TOP_MOVIES),
        Movie("mars-red-planet", "Mars: Red Planet", R.drawable.poster_12, null, "Sci-Fi", "2h 21min", "2024", "11+", 8.4f, "Astronauts confront a hostile new world.", MovieCategory.TOP_MOVIES),
    )

    private val sergel = Cinema("sergel", "Filmstaden Sergel", "Sveavägen 20", "Stockholm")
    private val scandinavia = Cinema("scandinavia", "Filmstaden Scandinavia", "Barkarbyvägen 25", "Järfälla")
    private val rigoletto = Cinema("rigoletto", "Filmstaden Rigoletto", "Kungsgatan 16", "Stockholm")
    private val heronCity = Cinema("heron-city", "Filmstaden Heron City", "Heron City, Kungens Kurva", "Stockholm")

    fun getMovies(category: MovieCategory? = null): List<Movie> =
        if (category == null) movies else movies.filter { it.category == category }

    fun getMovie(id: String): Movie = movies.first { it.id == id }

    fun getCinemas(): List<Cinema> = listOf(sergel, scandinavia, rigoletto, heronCity)

    fun getSelectedCinema(): Cinema = sergel

    fun getAvailableDates(): List<Pair<String, Int>> = listOf(
        "Thu" to 16,
        "Fri" to 17,
        "Sat" to 18,
        "Sun" to 19,
        "Mon" to 20
    )

    fun getAvailableTimes(): List<String> = listOf("16:00", "17:00", "18:00", "19:00", "20:00")

    fun getSeatLayout(): List<List<Seat>> {
        val reservedA = setOf(4)
        val reservedB = setOf(3, 9)
        val reservedC = setOf(4)
        val reservedD = setOf(3, 8)
        val reservedE = setOf<Int>()
        val reservedF = setOf(4)
        val reservedG = setOf(3)

        fun row(letter: Char, count: Int, tier: SeatTier, reserved: Set<Int>, selectedIndices: Set<Int> = emptySet()): List<Seat> {
            return (1..count).map { n ->
                Seat(
                    row = letter,
                    number = n,
                    tier = tier,
                    status = when {
                        n in selectedIndices -> SeatStatus.SELECTED
                        n in reserved -> SeatStatus.RESERVED
                        else -> SeatStatus.AVAILABLE
                    }
                )
            }
        }

        return listOf(
            row('A', 8, SeatTier.BEST, reservedA),
            row('B', 9, SeatTier.BEST, reservedB),
            row('C', 9, SeatTier.BETTER, reservedC),
            row('D', 9, SeatTier.BETTER, reservedD),
            row('E', 10, SeatTier.GOOD, reservedE, selectedIndices = setOf(6, 7)),
            row('F', 9, SeatTier.GOOD, reservedF),
            row('G', 7, SeatTier.GOOD, reservedG),
        )
    }

    fun getTickets(): List<Ticket> {
        val qr = "FS-${System.currentTimeMillis()}"
        return (1..4).map { n ->
            Ticket(
                id = "ticket-$n",
                movie = hailMary,
                cinema = sergel,
                date = "April 18",
                time = "6 p.m.",
                row = "E",
                seatNumber = (171 + n).toString(),
                qrData = "$qr-$n",
                ticketNumber = n,
                totalTickets = 4
            )
        }
    }

    fun getUserProfile() = UserProfile(
        name = "Jackson Mafra",
        email = "jackson.mafra@umain.com",
        memberNumber = "HH7G64",
        orders = 12,
        points = 759,
        spentKr = 2890,
        notificationsEnabled = true
    )
}
