package com.example.moview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moview.data.User
import com.example.moview.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    fun register(nome: String, senha: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val userExists = userDao.getUserByName(nome)
            if (userExists != null) {
                onError("Usuário já existe")
            } else {
                val user = User(nome = nome, senha = senha)
                try {
                    userDao.insert(user)
                    onSuccess()
                } catch (e: Exception) {
                    onError("Erro ao registrar usuário: ${e.message}")
                }
            }
        }
    }

    fun login(nome: String, senha: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByName(nome)
            if (user != null && user.senha == senha) {
                onResult(user)
            } else {
                onResult(null)
            }
        }
    }
}
