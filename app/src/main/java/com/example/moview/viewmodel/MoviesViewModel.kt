package com.example.moview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moview.data.model.Movie
import com.example.moview.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<MutableList<Movie>>()
    val movies: LiveData<MutableList<Movie>> get() = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var currentPage = 1

    init {
        _movies.value = mutableListOf()
    }

    fun fetchMovies(apiKey: String, page: Int) {
        _isLoading.value = true
        viewModelScope.launch {

            val movieResponse = repository.getPopularMovies(apiKey, page)
            _movies.value?.addAll(movieResponse.results)
            _isLoading.value = false
        }
    }
}

