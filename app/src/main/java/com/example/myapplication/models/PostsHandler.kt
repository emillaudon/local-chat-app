package com.example.myapplication.models

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp

class PostsHandler(application: Application, context: Context) {
    private var context = context
    private var application = application
    private var user = User

    public var posts: ArrayList<Post> = arrayListOf()

    fun getPosts(callback: () -> Unit) {
        val cacheHandler = PostCacheHandler(application, context)

        if(NetworkHandler.isOnline(context)) {

            val db = FirebaseFirestore.getInstance()
            db.collection("posts")
                .whereGreaterThanOrEqualTo("temperature", User.temperature - 1)
                .whereLessThanOrEqualTo("temperature", User.temperature + 1)
                .addSnapshotListener { value, e ->

                    if (e != null) {
                        println("Listen failed." + e)
                        callback()
                        return@addSnapshotListener
                    }

                    posts.clear()
                    for (document in value!!) {
                        posts.add(
                            Post.Builder()
                                .text(document.data["text"] as String)
                                .userName(document.data["userName"] as String)
                                .date(document.data["date"] as Long)
                                .temperature(document.data["temperature"].toString().toInt())
                                .build()
                        )
                    }

                    posts = PostSorter.bubbleSort(posts)

                    cacheHandler.clearPostCacheAndAddNewPosts(posts)

                    callback()
                }
        } else {
            cacheHandler.getCachedPosts {cachedPosts ->
                cachedPosts.forEach {dbPost ->
                    posts.add(
                        Post.Builder()
                            .text(dbPost.text)
                            .userName(dbPost.userName)
                            .date(dbPost.date)
                            .temperature(dbPost.temperature)
                            .build()
                    )
                }

                posts = PostSorter.bubbleSort(posts)
                callback()
            }
        }
    }

    fun newPost(text: String, callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val post = Post.Builder()
            .text(text)
            .userName(user.name)
            .date(System.currentTimeMillis())
            .temperature(User.temperature)
            .build()

        val postHashMap = hashMapOf(
            "text" to post.getText(),
            "userName" to post.getUserName(),
            "date" to post.getDate(),
            "temperature" to post.getTemperature()
        )
        db.collection("posts")
            .add(postHashMap)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")

                callback()
            }
            .addOnFailureListener { e ->
                callback()
                println("Error adding document")
            }
    }
}