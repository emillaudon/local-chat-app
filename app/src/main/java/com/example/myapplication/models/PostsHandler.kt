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
                    posts.sortBy { it.getDate() }
                    posts.reverse()

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
                posts.sortBy { it.getDate() }
                posts.reverse()
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


    companion object {
        /*
            Bubble sort extension on array of post
            Sorting on post date
        */
        fun ArrayList<Post>.bubbleSort(): ArrayList<Post> {

            if (this.count() <= 2) return this

            while (true) {

                var swapped = false

                for (i in 0 until this.count()) {
                    if (this[i].getDate() < this[i + 1].getDate()) {
                        val holdingObject = this[i]

                        this[i] = this[i + 1]
                        this[i + 1] = holdingObject

                        swapped = true
                    }
                }

                if (!swapped) return this
            }
        }
    }
}