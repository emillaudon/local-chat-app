package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.io.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        if (auth.currentUser != null) {
//            User logged in redirect to main activity
//            startActivity(Intent(this, MainActivity::class.java))
        }

        db = FirebaseFirestore.getInstance()

        val userNameTextField = findViewById<EditText>(R.id.userNameTextField)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {

            auth.signInAnonymously().addOnCompleteListener(this) { task ->
                if (task.isSuccessful && auth.currentUser?.uid != null) {

                    val authUser = auth.currentUser

                    val user = hashMapOf(
                            "name" to userNameTextField.text.toString()
                    )

//                    Cache user name
                    val sharedPref = this?.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString("user_name", userNameTextField.text.toString())
                        apply()
                    }



                    db.collection("users").document(authUser!!.uid).set(user)
                        .addOnSuccessListener {
                            println("aaaaa DocumentSnapshot successfully written!")

//                            Go to main activity
                            startActivity(Intent(this, MainActivity::class.java))
                        }

                    Toast.makeText(baseContext, "Authentication succeeded.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }


        }




    }





}