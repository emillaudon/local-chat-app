package com.example.myapplication

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import javax.security.auth.callback.Callback

class PostsHandler(userName: String) {
    private var userName = userName;

    public var posts: ArrayList<Post> = arrayListOf()

    fun getPosts(callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (document in result) {
                    println(document.data["text"])
                    println("TAG" + "${document.id} => ${document.data}")

                    posts.add(Post.Builder()
                        .text(document.data["text"] as String)
                        .userName(document.data["userName"] as String)
                        .date(document.data["date"] as com.google.firebase.Timestamp)
                        .build()
                    )
                }
                posts.sortBy { it.getDate() }
                posts.reverse()
                callback()
            }
            .addOnFailureListener { exception ->
                callback()
                Log.w("TAG", "Error getting documents.", exception)
            }
    }

    fun newPost(text: String, callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val post = Post.Builder()
            .text(text)
            .userName(userName)
            .date(com.google.firebase.Timestamp.now())
            .build()

        val postHashMap = hashMapOf(
            "text" to post.getText(),
            "userName" to post.getUserName(),
            "date" to post.getDate()
        )
        db.collection("posts")
            .add(postHashMap)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                posts.add(post)
                posts.sortBy { it.getDate() }
                posts.reverse()
                callback()
            }
            .addOnFailureListener { e ->
                callback()
                println("Error adding document")
            }
    }
}