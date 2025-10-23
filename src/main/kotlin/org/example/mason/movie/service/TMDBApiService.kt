package org.example.mason.movie.service

import kotlinx.serialization.json.Json
import org.example.mason.movie.model.dto.MovieDetailResponse
import org.example.mason.movie.model.dto.PopularMovieApiResponse
import org.example.mason.movie.model.json.MovieDetail
import org.example.mason.movie.model.json.PopularMovieResponse
import org.example.mason.movie.model.json.TmdbConfig
import org.example.mason.movie.model.json.toMovieResponse
import org.example.mason.movie.model.json.toPopularMovieApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class TMDBApiService(
    @Value("\${tmdb.api.key}") private val tmdbApiKey: String,
    @Value("\${tmdb.api.base-url}") private val tmdbApiBaseUrl: String
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TMDBApiService::class.java)
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
            .baseUrl(tmdbApiBaseUrl)
            .exchangeStrategies(strategies) // 5. 套用上面建立的 strategies
            .defaultHeader(
                "Authorization",
                "Bearer $tmdbApiKey"
            )
            .defaultHeader(
                "Accept",
                "application/json"
            )
            .build()
        logger.info("WebClient has been initialized with base URL: $tmdbApiBaseUrl")
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
            logger.info("成功擷取熱門電影資料")
            originalResponse.toPopularMovieApiResponse()
        } catch (e: Exception) {
            logger.error("呼叫 TMDB 熱門電影 API 失敗", e)
            null // 發生任何例外時，回傳 null
        }
    }

    suspend fun getMovieDetail(id: Int, language: String = "zh-TW"): MovieDetailResponse? {
        return try {
            val movieDetail = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/movie")
                        .path("/${id}")
                        .queryParam("language", language)
                        .build()
                }
                .retrieve()
                .awaitBody<MovieDetail>()
            logger.info("電影詳情: $movieDetail")
            movieDetail.toMovieResponse()
        } catch (e: Exception) {
            logger.error("電影詳情呼叫失敗", e)
            null;
        }
    }


}