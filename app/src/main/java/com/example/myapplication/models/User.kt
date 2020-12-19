package com.example.myapplication.models

import android.app.Activity
import android.content.Context
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

    val secretKey = "1234567812345678"

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

    fun cache(activity: Activity) {

//       Cache user name
        val sharedPref = activity.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("user_name", name)
            putInt("temperature", temperature)
//           Add more values to cache here if needed
            apply()
        }

    }

    fun getFromCache(activity: Activity) {

        val sharedPref = activity.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        name = sharedPref.getString("user_name", "").toString()
        temperature = sharedPref.getInt("temperature", 0)
    }

}