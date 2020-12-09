package com.example.myapplication

import com.google.firebase.Timestamp
import java.util.*

class Post {
    private var text : String
    private var userName : String
    private var date : Timestamp

    constructor(userName: String, text: String, date: Timestamp){ this.text = text; this.userName = userName; this.date = date}


    fun getText(): String {
        return text
    }

    fun getUserName(): String {
        return userName
    }

    fun getDate(): Timestamp {
        return date
    }
}