package com.example.myapplication.models

object User {

    operator fun invoke(name: String, temperature: Int): User {
        User.name = name
        User.temperature = temperature

        return this
    }

    var name: String = "null"
    var temperature: Int = 0


}