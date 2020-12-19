package com.example.myapplication.models

import com.google.firebase.Timestamp

class Post private constructor(builder: Builder) {
    private var text : String?
    private var userName : String?
    private var date : Long?
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
        var date: Long? = null
        var temperature: Int? = null

        fun text(text: String) = apply { this.text = text }
        fun userName(userName: String) = apply { this.userName = userName }
        fun date(date: Long) = apply { this.date = date }
        fun temperature(temperature: Int) = apply { this.temperature = temperature }
        fun build() = Post(this)
    }


    fun getText(): String {
        return text as String
    }

    fun getUserName(): String {
        return userName as String
    }

    fun getDate(): Long {
        return date as Long
    }

    fun getTemperature() : Int {
        return temperature as Int
    }
}