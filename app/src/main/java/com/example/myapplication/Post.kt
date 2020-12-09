package com.example.myapplication

class Post {
    private var text : String
    private var userName : String

    constructor(userName: String, text: String){ this.text = text; this.userName = userName}


    fun getText(): String {
        return text
    }

    fun getUserName(): String {
        return userName
    }
}