package com.example.myapplication

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


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<PostAdapter.ViewHolder>

    private lateinit var fab : FloatingActionButton

    private var posts: ArrayList<Post> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        val refresher = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)


        val db = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerView)
        adapter = PostAdapter(posts, this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)



        for (i in 0..1) {
            val post = Post(
                "heading" + (i + 1),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
            posts.add(post)
        }

        getPosts()

        refresher.setOnRefreshListener {
            getPosts()
            refresher.isRefreshing = false
        }

        fab.setOnClickListener {
            fabClicked()
        }

    }

    private fun fabClicked() {
        goToPostCreator()
    }

    private fun getPosts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (document in result) {
                    println(document.data["text"])
                    println("TAG" + "${document.id} => ${document.data}")

                    posts.add(Post(
                        document.data["text"] as String,
                        document.data["userName"] as String
                    ))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    private fun newPost(text: String) {
        val db = FirebaseFirestore.getInstance()
        val post = Post(
            "TestUser",
            text
        )
        val postHashMap = hashMapOf(
            "text" to post.getText(),
            "userName" to post.getUserName()
        )
        db.collection("posts")
            .add(postHashMap)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                posts.add(post)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                println("Error adding document")
            }

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
                newPost(newPostText)
            }
        }
    }

}
