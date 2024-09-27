package com.example.moview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUser(id: Long): User?

    @Query("SELECT * FROM usuarios WHERE nome = :name")
    suspend fun getUserByName(name: String): User?
}
