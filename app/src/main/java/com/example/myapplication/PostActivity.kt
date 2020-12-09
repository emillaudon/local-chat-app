package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    private lateinit var postEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postEditText = findViewById(R.id.editTextPost)

        val postButton = findViewById<Button>(R.id.postbutton)

        postButton.setOnClickListener {
            println("ff")
            finishedPost()
        }
    }

    private fun finishedPost() {
        val postText = postEditText.text.toString()

        val intentWithResult = Intent()
        intentWithResult.putExtra("result", postText)

        setResult(Activity.RESULT_OK, intentWithResult)
        finish()
    }
}
