package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.*

@Entity(tableName = "post_table")
data class DBPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var text : String,
    val userName : String,
    val date : Long,
    val temperature : Int
)