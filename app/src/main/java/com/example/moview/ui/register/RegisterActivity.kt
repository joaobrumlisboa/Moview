package com.example.moview.ui.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.moview.R
import com.example.moview.ui.login.LoginActivity
import com.example.moview.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextSenha = findViewById<EditText>(R.id.editTextSenha)
        val buttonRegister = findViewById<Button>(R.id.btnRegister)
        val buttonLogin = findViewById<Button>(R.id.btnLogin)

        // Navegação para a tela de login
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a activity atual ao voltar para a tela de login
        }

        // Registro do usuário
        buttonRegister.setOnClickListener {
            val nome = editTextNome.text.toString()
            val senha = editTextSenha.text.toString()

            if (nome.isNotEmpty() && senha.isNotEmpty()) {
                userViewModel.register(nome, senha, {
                    // Exibir Toast na parte superior
                    Toast.makeText(this, "usuário registrado com sucesso", Toast.LENGTH_SHORT).show()

                    // Limpar campos
                    editTextNome.text.clear()
                    editTextSenha.text.clear()

                    // Adicionar delay de 2 segundos antes de redirecionar para o login
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Evita que a LoginActivity seja reiniciada
                        startActivity(intent)
                        finish() // Finaliza a RegisterActivity após iniciar a LoginActivity
                    }, 2000) // 2 segundos de delay

                }, {
                    // Exibir Toast de erro
                    Toast.makeText(this, "Erro ao registrar usuário", Toast.LENGTH_SHORT).show()

                    // Limpar campos
                    editTextNome.text.clear()
                    editTextSenha.text.clear()
                })
            } else {
                // Exibir Toast se os campos estiverem vazios
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
