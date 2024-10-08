package com.example.moview.ui.detail_movies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.moview.R
import com.example.moview.data.repository.MovieRepository
import com.example.moview.viewmodel.DetailMoviesViewModel
import com.example.moview.viewmodel.DetailMoviesViewModelFactory
import com.squareup.picasso.Picasso

class DetailMoviesActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailMoviesViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        setTheme(if (isDarkMode) R.style.Theme_Moview_Dark else R.style.Theme_Moview)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movies)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = null


        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1) {
            viewModel = DetailMoviesViewModelFactory(MovieRepository(), movieId, this).create(DetailMoviesViewModel::class.java)
            viewModel.fetchMovieDetails()


            viewModel.movieDetail.observe(this, { movie ->
                val movieImage: ImageView = findViewById(R.id.movie_image)
                val movieTitle: TextView = findViewById(R.id.movie_title)
                val movieYear: TextView = findViewById(R.id.movie_year)
                val movieOverview: TextView = findViewById(R.id.movie_overview)

                movieTitle.text = movie.title
                movieYear.text = movie.release_date.split("-")[0] // Extrair apenas o ano
                movieOverview.text = movie.overview

                Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    .into(movieImage)
            })
        } else {
            finish()
        }

        val backButton: ImageButton = findViewById(R.id.logout_back_button)
        updateBackButtonIcon(backButton, isDarkMode)
        backButton.setOnClickListener {
            finish()
        }

        val themeButton: ImageButton = findViewById(R.id.theme_button)
        updateThemeIcon(themeButton, isDarkMode)

        themeButton.setOnClickListener {

            val newDarkMode = !isDarkMode
            sharedPreferences.edit().putBoolean("dark_mode", newDarkMode).apply()
            recreate()
        }
    }

    private fun updateBackButtonIcon(button: ImageButton, isDarkMode: Boolean) {
        if (isDarkMode) {
            button.setImageResource(R.drawable.setapreta)
        } else {
            button.setImageResource(R.drawable.seta)
        }
    }

    private fun updateThemeIcon(button: ImageButton, isDarkMode: Boolean) {
        if (isDarkMode) {
            button.setImageResource(R.drawable.lua)
        } else {
            button.setImageResource(R.drawable.sol)
        }
    }
}
