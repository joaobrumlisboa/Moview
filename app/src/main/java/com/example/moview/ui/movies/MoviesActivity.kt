package com.example.moview.ui.movies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moview.R
import com.example.moview.data.repository.MovieRepository
import com.example.moview.ui.detail_movies.DetailMoviesActivity
import com.example.moview.ui.login.LoginActivity
import com.example.moview.viewmodel.MoviesViewModel
import com.example.moview.viewmodel.MoviesViewModelFactory

class MoviesActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(MovieRepository())
    }

    private var currentPage = 1
    private lateinit var loadingLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        setTheme(if (isDarkMode) R.style.Theme_Moview_Dark else R.style.Theme_Moview)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)


        val themeButton: ImageButton = findViewById(R.id.theme_button)
        val logoutButton: ImageButton = findViewById(R.id.logout_back_button)


        updateLogoutButtonIcon(logoutButton, isDarkMode)

        updateThemeIcon(themeButton, isDarkMode)

        themeButton.setOnClickListener {

            val newDarkMode = !isDarkMode
            sharedPreferences.edit().putBoolean("dark_mode", newDarkMode).apply()

            updateLogoutButtonIcon(logoutButton, newDarkMode)
            recreate()
        }


        logoutButton.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        loadingLayout = findViewById(R.id.loading_layout)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val apiKey = getString(R.string.tmdb_api_key)


        viewModel.movies.observe(this, Observer { movies ->
            recyclerView.adapter = MoviesAdapter(movies) { movie ->

                val intent = Intent(this, DetailMoviesActivity::class.java)
                intent.putExtra("movie_id", movie.id)
                startActivity(intent)
            }
            showLoading(false) // Oculta o loading após os filmes serem carregados
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading) // Controla a visibilidade do layout de loading
        })


        if (savedInstanceState == null) {
            showLoading(true) // Mostrar loading ao iniciar
            viewModel.fetchMovies(apiKey, currentPage) // Carregar filmes ao iniciar a atividade
        }


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Verifica se o usuário rolou até o final
                if (!recyclerView.canScrollVertically(1)) { // 1 para verificar o final
                    // Carregar mais filmes
                    if (!viewModel.isLoading.value!!) { // Verifica se não está carregando
                        currentPage++
                        viewModel.fetchMovies(apiKey, currentPage) // Chama a função com a próxima página
                    }
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateThemeIcon(button: ImageButton, isDarkMode: Boolean) {
        if (isDarkMode) {
            button.setImageResource(R.drawable.lua)  // Ícone sol para voltar ao modo claro
        } else {
            button.setImageResource(R.drawable.sol)  // Ícone da lua para ativar o modo escuro
        }
    }

    private fun updateLogoutButtonIcon(button: ImageButton, isDarkMode: Boolean) {
        if (isDarkMode) {
            button.setImageResource(R.drawable.logoutpreto) // Use um ícone escuro para o modo escuro
        } else {
            button.setImageResource(R.drawable.logout) // Ícone padrão para o modo claro
        }
    }
}
