package com.example.myapplication.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.LruCache
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.models.IconHandler
import com.example.myapplication.models.NetworkHandler
import com.example.myapplication.models.TemperatureHandler
import com.example.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.net.URL


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val temperatureHandler =
        TemperatureHandler(this)

    private lateinit var memoryCache: LruCache<String, Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        User("undefined", 0)

        auth = Firebase.auth


        if (!NetworkHandler.isOnline(this)) {
            Toast.makeText(baseContext, "No network.", Toast.LENGTH_SHORT).show()

            User.getFromCache(this)

//            TODO: show only cached post in MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }

        IconHandler.createCache()

        temperatureHandler.getTemperature { temperature ->
            if (auth.currentUser != null) {
//                Get cached user object update user temp
                User.uid = auth.currentUser!!.uid
                User.getFromCache(this)
                User.temperature = temperature

//               User logged in redirect to main activity
                checkIconsCacheThenRedirect()
            }
        }


        val userNameTextField = findViewById<EditText>(R.id.userNameTextField)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        if (auth.currentUser == null)
            animateEditTextAndButton()

        loginBtn.setOnClickListener {
            auth.signInAnonymously().addOnCompleteListener(this) { task ->
                if (task.isSuccessful && auth.currentUser?.uid != null) {

                    User.name = userNameTextField.text.toString()
                    User.uid = auth.currentUser!!.uid

                    User.cache(this)
                    User.saveToDb {
                        checkIconsCacheThenRedirect()
                    }
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
        if (requestCode == 1) setUserTemperature()
    }

    private fun setUserTemperature() {

        temperatureHandler.getTemperature { temperature ->
            User.temperature = temperature
        }
    }


//    TODO: do better :(
    fun checkIconsCacheThenRedirect() {
        if (!IconHandler.isCached()) {
            IconHandler.cacheIconsFromDb() {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}