package com.example.myapplication.activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PostAdapter
import com.example.myapplication.models.EncryptionHandler
import com.example.myapplication.models.PostsHandler
import com.example.myapplication.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<PostAdapter.ViewHolder>

    private lateinit var fab : FloatingActionButton

    private lateinit var postsHandler : PostsHandler
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = EncryptionHandler.decrypt(User.name) + "     " + User.temperature + "°C"

        postsHandler = PostsHandler(application, this)
        postsHandler.getPosts() {
            adapter.notifyDataSetChanged()
        }

        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = PostAdapter(
            postsHandler.posts,
            this
        )
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            fabClicked()
        }


        val x = EncryptionHandler.encrypt("hej")

        println("enc:" + x)

        val y = EncryptionHandler.decrypt(x)
        println("enc: dec " + y)

    }

    private fun fabClicked() {
        goToPostCreator()
    }

    private fun goToPostCreator() {
        val intent : Intent = Intent(this, PostActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null && requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val newPostText = data.getStringExtra("result")
                postsHandler.newPost(newPostText) {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

}
