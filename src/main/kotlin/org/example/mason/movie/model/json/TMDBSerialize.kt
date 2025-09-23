package org.example.mason.movie.model.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.mason.movie.model.dto.MovieApiResponse
import org.example.mason.movie.model.dto.PopularMovieApiResponse

@Serializable
data class TmdbConfig(
    val images: ImagesConfig,
    @SerialName("change_keys") // 將 JSON 的 snake_case 對應到駝峰式命名
    val changeKeys: List<String>
)

@Serializable
data class ImagesConfig(
    @SerialName("base_url")
    val baseUrl: String,
    @SerialName("secure_base_url")
    val secureBaseUrl: String,
    @SerialName("poster_sizes")
    val posterSizes: List<String>
)


@Serializable
data class PopularMovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

fun PopularMovieResponse.toPopularMovieApiResponse() : PopularMovieApiResponse {
    return PopularMovieApiResponse(
        page = this.page,
        results = this.results.map { movie ->
            MovieApiResponse(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage
            )
        },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}

// 代表 results 列表中的單一電影物件
@Serializable
data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Double
)