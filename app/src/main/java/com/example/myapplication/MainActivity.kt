package com.example.myapplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<PostAdapter.ViewHolder>

    private lateinit var fab : FloatingActionButton

    private lateinit var userName : String

    private lateinit var postsHandler : PostsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = User.name + " " + User.temperature + "°C"

        val sharedPref = this?.getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        userName = sharedPref.getString("user_name", "").toString()
        postsHandler = PostsHandler(userName)
        postsHandler.getPosts() {
            adapter.notifyDataSetChanged()
        }

        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = PostAdapter(postsHandler.posts, this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val refresher = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        refresher.setOnRefreshListener {
            postsHandler.getPosts() {
                refresher.isRefreshing = false
                adapter.notifyDataSetChanged()
            }
        }

        fab.setOnClickListener {
            fabClicked()
        }

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
