package com.example.moview.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moview.R
import com.example.moview.ui.movies.MoviesActivity
import com.example.moview.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.example.moview.ui.register.RegisterActivity

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        setTheme(if (isDarkMode) R.style.Theme_Moview_Dark else R.style.Theme_Moview)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextSenha = findViewById<EditText>(R.id.editTextSenha)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)
        val buttonRegister = findViewById<Button>(R.id.btnRegister)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val nome = editTextNome.text.toString()
            val senha = editTextSenha.text.toString()

            if (nome.isNotEmpty() && senha.isNotEmpty()) {
                userViewModel.login(nome, senha) { user ->
                    if (user != null) {
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MoviesActivity::class.java)
                        startActivity(intent)
                        editTextNome.text.clear()
                        editTextSenha.text.clear()
                        finish()
                    } else {
                        Toast.makeText(this, "Usuário e/ou senha incorretos", Toast.LENGTH_SHORT).show()
                        editTextNome.text.clear()
                        editTextSenha.text.clear()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
