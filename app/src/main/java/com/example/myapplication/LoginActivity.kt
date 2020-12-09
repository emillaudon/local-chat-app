package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        if (auth.currentUser != null) {
//            User logged in redirect to main activity
            startActivity(Intent(this, MainActivity::class.java))
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
//                    TODO: Cache user name

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