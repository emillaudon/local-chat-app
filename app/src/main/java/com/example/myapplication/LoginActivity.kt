package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.io.*
import kotlin.properties.Delegates


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        Ask for location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LoginActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

        getTemperature()

        if (auth.currentUser != null) {
//            User logged in redirect to main activity
            val sharedPref = this?.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
            var userName = sharedPref.getString("user_name", "").toString()
            User(userName, 0)
            startActivity(Intent(this, MainActivity::class.java))
        }

        db = FirebaseFirestore.getInstance()

        val userNameTextField = findViewById<EditText>(R.id.userNameTextField)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) getTemperature()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    fun getTemperature() {

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            val temperatureHandler = TemperatureHandler(location)

            temperatureHandler.getTemperature { temperature ->
//                TODO: Save in User class or send to next activity
            }
        }
    }



}