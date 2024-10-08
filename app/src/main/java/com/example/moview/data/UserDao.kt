package com.example.moview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUser(id: Long): User?

    @Query("SELECT * FROM usuarios WHERE nome = :name")
    fun getUserByName(name: String): User?
}
