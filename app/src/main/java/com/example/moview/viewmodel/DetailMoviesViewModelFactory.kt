package com.example.moview.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moview.data.repository.MovieRepository

class DetailMoviesViewModelFactory(
    private val repository: MovieRepository,
    private val movieId: Int,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailMoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailMoviesViewModel(repository, movieId, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
