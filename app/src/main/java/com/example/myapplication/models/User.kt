package com.example.myapplication.models

import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore

object User {

    operator fun invoke(name: String, temperature: Int): User {
        User.name = name
        User.temperature = temperature

        return this
    }

    var name: String = "null"
    var temperature: Int = 0
    var uid: String? = null

    private lateinit var db: FirebaseFirestore

    fun saveToDb(callback: () -> Unit) {

        if (uid == null) return

        db = FirebaseFirestore.getInstance()

        val user    = hashMapOf(
            "name" to name
        )

        db.collection("users").document(uid!!).set(user)
            .addOnSuccessListener {
                println("User successfully saved!")

                callback()
            }

    }

}