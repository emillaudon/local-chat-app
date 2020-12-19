package com.example.myapplication.data

import androidx.lifecycle.LiveData

class PostRepository(private val postDao: PostDao) {

    val readAllData: LiveData<List<DBPost>> = postDao.readAllData()

    suspend fun addPost(post: com.example.myapplication.models.Post) {
        var dbPost = convertToDbPost(post)

        postDao.addPost(dbPost)
    }

    suspend fun addPosts(posts: List<com.example.myapplication.models.Post>) {
        var dbPosts = mutableListOf<DBPost>()
        posts.forEach({post ->
            dbPosts.add(convertToDbPost(post))
        })
        postDao.addPosts(dbPosts)
    }

    private fun convertToDbPost(post: com.example.myapplication.models.Post) : DBPost {
        return DBPost(
            0, post.getText(), post.getUserName(), post.getDate(), post.getTemperature()
        )
    }
}