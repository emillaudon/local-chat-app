package com.example.myapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class PostActivity : AppCompatActivity() {
    private lateinit var postEditText : EditText

    private lateinit var imm : InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        title = "Create Post"

        postEditText = findViewById(R.id.editTextPost)

        focusEditText()

        val postButton = findViewById<Button>(R.id.postbutton)

        postButton.setOnClickListener {
            if(postEditText.text != null && postEditText.text.length > 1) {
                finishedPost()
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            } else {
                Toast.makeText(baseContext, "You have to type something.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun focusEditText() {
        imm =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        postEditText.requestFocus()
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun finishedPost() {
        val postText = postEditText.text.toString()

        val intentWithResult = Intent()
        intentWithResult.putExtra("result", postText)

        setResult(Activity.RESULT_OK, intentWithResult)
        finish()
    }
}
