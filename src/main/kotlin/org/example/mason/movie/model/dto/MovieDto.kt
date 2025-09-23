package org.example.mason.movie.model.dto



data class MovieApiResponse(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double
)

data class PopularMovieApiResponse(
    val page: Int,
    val results: List<MovieApiResponse>,
    val totalPages: Int,
    val totalResults: Int
)