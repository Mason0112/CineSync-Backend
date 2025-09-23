package org.example.mason.movie.service

import kotlinx.serialization.json.Json
import org.example.mason.movie.model.dto.PopularMovieApiResponse
import org.example.mason.movie.model.json.PopularMovieResponse
import org.example.mason.movie.model.json.TmdbConfig
import org.example.mason.movie.model.json.toPopularMovieApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class TMDBApiService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TMDBApiService::class.java)
        const val TMDB_API_BASE_URL = "https://api.themoviedb.org/3"
        const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val TMDB_API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhZDM0Y2ZkM2MzYzA4ZWYyOThjNTZkYzdlODFjMjAxYiIsIm5iZiI6MTc1ODE4OTA3MS45NjUsInN1YiI6IjY4Y2JkNjBmNjFjMmIwYTM0YWNmNDY0MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.QQvwy2IvhOJrtWZcNXv-MNQY7DseXmY4HYPbz7hP4ME"
    }

    private val webClient: WebClient

    init {
        val strategies = ExchangeStrategies.builder()
            .codecs {
                it.defaultCodecs().kotlinSerializationJsonDecoder(
                    // 忽略 JSON 中有但 Data Class 中沒有的欄位
                    KotlinSerializationJsonDecoder(Json { ignoreUnknownKeys = true })
                )
            }
            .build()

        // 在 init 區塊中，對宣告好的 webClient 屬性進行賦值
        webClient = WebClient.builder()
            .baseUrl(TMDB_API_BASE_URL)
            .exchangeStrategies(strategies) // 5. 套用上面建立的 strategies
            .defaultHeader(
                "Authorization",
                "Bearer $TMDB_API_KEY"
            )
            .defaultHeader(
                "Accept",
                "application/json"
            )
            .build()
        logger.info("WebClient has been initialized.")
    }

    suspend fun callConfig() {
        try {

            val configResponse = webClient.get().uri("/configuration").retrieve().awaitBody<TmdbConfig>()
            logger.info("Successfully fetched config. Secure base URL: {}", configResponse.images.secureBaseUrl)
            logger.debug("Full config response: {}", configResponse)
        } catch (e: Exception) {
            logger.error("Failed to call TMDB configuration API", e)
        }
    }

    suspend fun getPopularMovies(page: Int = 1, language: String = "zh-TW"): PopularMovieApiResponse? {
        return try {
            logger.info("正在擷取第 {} 頁的熱門電影...", page)

            val originalResponse = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/discover/movie")
                        .queryParam("include_adult", false)
                        .queryParam("language", language)
                        .queryParam("page", page)
                        .queryParam("sort_by", "popularity.desc")
                        .build()
                }
                .retrieve()
                .awaitBody<PopularMovieResponse>()

            originalResponse.copy(
                results = originalResponse.results.map { movie ->
                    movie.posterPath?.let { path ->
                        movie.copy(posterPath = TMDB_IMAGE_BASE_URL + path)
                    } ?: movie
                }
            ).toPopularMovieApiResponse()
        } catch (e: Exception) {
            logger.error("呼叫 TMDB 熱門電影 API 失敗", e)
            null // 發生任何例外時，回傳 null
        }
    }

}