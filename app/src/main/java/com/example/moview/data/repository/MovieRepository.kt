package com.example.moview.data.repository

import com.example.moview.data.api.RetrofitInstance
import com.example.moview.data.api.TmdbApi
import com.example.moview.data.model.MovieDetails
import com.example.moview.data.model.MovieResponse

class MovieRepository {
    suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse {
        return RetrofitInstance.api.getPopularMovies(apiKey, page)
    }

    suspend fun getMovieDetails(movieId: Int, apiKey: String): MovieDetails {
        return RetrofitInstance.api.getMovieDetails(movieId, apiKey)
    }
}


