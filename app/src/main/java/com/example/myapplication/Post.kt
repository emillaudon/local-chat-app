package com.example.myapplication

import com.google.firebase.Timestamp
import java.util.*

class Post private constructor(builder: Post.Builder) {
    private var text : String?
    private var userName : String?
    private var date : Timestamp?
    private var temperature : Int?

    init {
        this.text = builder.text
        this.userName = builder.userName
        this.date = builder.date
        this.temperature = builder.temperature
    }

    class Builder {
        var text: String? = null
            private set
        var userName: String? = null
            private set
        var date: Timestamp? = null
        var temperature: Int? = null

        fun text(text: String) = apply { this.text = text }
        fun userName(userName: String) = apply { this.userName = userName }
        fun date(date: Timestamp) = apply { this.date = date }
        fun temperature(temperature: Int) = apply { this.temperature = temperature }
        fun build() = Post(this)
    }


    fun getText(): String? {
        return text
    }

    fun getUserName(): String? {
        return userName
    }

    fun getDate(): Timestamp? {
        return date
    }

    fun getTemperature() : Int? {
        return temperature
    }
}