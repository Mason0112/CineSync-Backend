package org.example.mason.movie.controller

import org.example.mason.movie.model.dto.MovieDetailResponse
import org.example.mason.movie.model.dto.PopularMovieApiResponse
import org.example.mason.movie.model.json.MovieDetail
import org.example.mason.movie.model.json.PopularMovieResponse
import org.example.mason.movie.service.TMDBApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class TMDataBaseController(TMDBApiService: TMDBApiService) {


    private val tMDBApiService: TMDBApiService = TMDBApiService


    @GetMapping("/popular")
    suspend fun getPopularMovies(
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "language", defaultValue = "zh-TW") language: String
    ): ResponseEntity<PopularMovieApiResponse> {

        return tMDBApiService.getPopularMovies(page, language)?.let { popularMovies ->
            // 如果 getPopularMovies 的結果不是 null，則執行這個 let 區塊
            ResponseEntity.ok(popularMovies)
        } ?: ResponseEntity.internalServerError().build() // 如果結果是 null，則回傳 500
    }

    @GetMapping("/detail/{id}")
    suspend fun getMovieDetails(
        @PathVariable("id") id: Int,
        @RequestParam(name = "language", defaultValue = "zh-TW") language: String
    ) : ResponseEntity<MovieDetailResponse>{
        return tMDBApiService.getMovieDetail(id, language)?.let { movieDetail ->
            ResponseEntity.ok(movieDetail)
        } ?: ResponseEntity.internalServerError().build()
    }
}