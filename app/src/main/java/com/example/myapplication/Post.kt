package com.example.myapplication

class Post {
    private var header : String
    private var text : String

    constructor(header: String, text: String){this.header = header; this.text = text}

    fun getHeader(): String {
        return header
    }

    fun getText(): String {
        return text
    }
}