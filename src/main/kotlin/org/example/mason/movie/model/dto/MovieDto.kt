package org.example.mason.movie.model.dto


data class MovieApiResponse(
    val id: Long,
    val title: String,
    val overview: String,
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

data class MovieDetailResponse(
    val id: Int,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<GenreDto>,
    val releaseDate: String,
    val overview: String,
    val title: String,
    val productionCompanies: List<ProductionCompanyDto>

)

data class GenreDto(
    val id: Int,
    val name: String
)

data class ProductionCompanyDto(
    val id: Int,
    val logoPath: String?,
    val name: String
)