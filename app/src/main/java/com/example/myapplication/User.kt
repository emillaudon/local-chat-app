package com.example.myapplication

object User {

    operator fun invoke(name: String, temperature: Int): User {
        this.name = name
        this.temperature = temperature

        return this
    }

    var name: String = "null"
    var temperature: Int = 0


}