package com.example.moview.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moview.R
import com.example.moview.data.model.MovieDetails
import com.example.moview.data.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailMoviesViewModel(
    private val repository: MovieRepository,
    private val movieId: Int,
    private val context: Context
) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetail: LiveData<MovieDetails> get() = _movieDetails

    init {
        fetchMovieDetails()
    }

    fun fetchMovieDetails() {
        viewModelScope.launch {
            val apiKey = context.getString(R.string.tmdb_api_key)
            val details = repository.getMovieDetails(movieId, apiKey)
            _movieDetails.postValue(details)
        }
    }
}
