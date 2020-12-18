package com.example.myapplication.models

import com.google.firebase.firestore.FirebaseFirestore

class PostsHandler() {
    private var user = User

    public var posts: ArrayList<Post> = arrayListOf()

    fun getPosts(callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .whereGreaterThanOrEqualTo("temperature", User.temperature - 1)
            .whereLessThanOrEqualTo("temperature", User.temperature + 1)
            .addSnapshotListener { value, e  ->

                if (e != null) {
                    println( "Listen failed." + e)
                    callback()
                    return@addSnapshotListener
                }

                posts.clear()
                for (document in value!!) {

                    posts.add(
                        Post.Builder()
                        .text(document.data["text"] as String)
                        .userName(document.data["userName"] as String)
                        .date(document.data["date"] as com.google.firebase.Timestamp)
                        .temperature(document.data["temperature"].toString().toInt())
                        .build()
                    )
                }
                posts.sortBy { it.getDate() }
                posts.reverse()
                callback()
            }
    }

    fun newPost(text: String, callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val post = Post.Builder()
            .text(text)
            .userName(user.name)
            .date(com.google.firebase.Timestamp.now())
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