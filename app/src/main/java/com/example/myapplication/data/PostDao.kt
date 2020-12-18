package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPost(post: DBPost)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPosts(posts: List<DBPost>)

    @Query("SELECT * FROM post_table ORDER BY date ASC")
    fun readAllData(): LiveData<List<DBPost>>

    @Query("SELECT * FROM post_table")
    fun readAllDataa(): List<DBPost>


    @Query("DELETE FROM post_table")
    fun nukeTable()
}