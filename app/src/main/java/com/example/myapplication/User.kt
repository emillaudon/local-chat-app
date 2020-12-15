package com.example.myapplication

object User {

    operator fun invoke(name: String, temperature: Int): User {
        this.name = name
        this.temperature = temperature

        return this
    }

    lateinit var name: String
    var temperature: Int = 0

    //  TODO: Add private var coordinates ?




}