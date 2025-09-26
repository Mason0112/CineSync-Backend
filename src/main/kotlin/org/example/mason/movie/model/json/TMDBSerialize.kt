package org.example.mason.movie.model.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.mason.movie.model.dto.MovieApiResponse
import org.example.mason.movie.model.dto.MovieDetailResponse
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

fun PopularMovieResponse.toPopularMovieApiResponse(): PopularMovieApiResponse {
    // 定義圖片基底網址，或從設定檔讀取
    val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    return PopularMovieApiResponse(
        page = this.page,
        results = this.results.map { movie ->
            MovieApiResponse(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.posterPath?.let { path -> imageBaseUrl + path },
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

@Serializable
data class Genres(
    val id: Int,
    val name: String
)

@Serializable
data class MovieDetail(
    val id: Int,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val budget: Int,
    val genres: List<Genres>,
    @SerialName("release_date")
    val releaseDate: String,
    val overview: String,
    val title: String,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>
)

@Serializable
data class ProductionCompany(
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    val name: String
)

fun MovieDetail.toMovieResponse(): MovieDetailResponse {
    return MovieDetailResponse(
        id = this.id,
        backdropPath = this.backdropPath,
        budget = this.budget,
        genres = this.genres.map { genre ->
            org.example.mason.movie.model.dto.GenresDto(
                id = genre.id,
                name = genre.name
            )
        },
        releaseDate = this.releaseDate,
        overview = this.overview,
        title = this.title,
        productionCompanies = this.productionCompanies.map { productionCompany ->
            org.example.mason.movie.model.dto.ProductionCompanyDto(
                id = productionCompany.id,
                logoPath = productionCompany.logoPath,
                name = productionCompany.name
            )
        }
    )
}
