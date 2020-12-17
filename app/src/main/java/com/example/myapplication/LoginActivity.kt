package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        User("undefined", 0)

        auth = Firebase.auth

        val temperatureHandler = TemperatureHandler(this)

        temperatureHandler.getTemperature { temperature ->
            User.temperature = temperature.toInt()

            if (auth.currentUser != null) {
//            User logged in redirect to main activity
                val sharedPref = this?.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
                val userName = sharedPref.getString("user_name", "").toString()
                User.name = userName
                startActivity(Intent(this, MainActivity::class.java))
            }
        }


        db = FirebaseFirestore.getInstance()

        val userNameTextField = findViewById<EditText>(R.id.userNameTextField)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        if (auth.currentUser == null)
            animateEditTextAndButton()

        loginBtn.setOnClickListener {
            User.name = userNameTextField.text.toString()

            auth.signInAnonymously().addOnCompleteListener(this) { task ->
                if (task.isSuccessful && auth.currentUser?.uid != null) {

                    val authUser        = auth.currentUser
                    val user    = hashMapOf(
                            "name" to userNameTextField.text.toString()
                    )

//                    Cache user name
                    val sharedPref = this?.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString("user_name", userNameTextField.text.toString())
                        apply()
                    }

//                    Save user to db
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

    private fun animateEditTextAndButton() {
        userNameTextField.visibility = View.VISIBLE
        loginBtn.visibility = View.VISIBLE
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 1500
        userNameTextField.startAnimation(fadeIn)
        loginBtn.startAnimation(fadeIn)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) getTemperature()
    }

    @SuppressLint("MissingPermission")
    fun getTemperature() {

        val temperatureHandler = TemperatureHandler(this)

        temperatureHandler.getTemperature { temperature ->
            User.temperature = temperature.toInt()
        }

    }



}