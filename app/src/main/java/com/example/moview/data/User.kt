package com.example.moview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val senha: String
)
