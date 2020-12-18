package com.example.myapplication.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.myapplication.data.DBPost
import com.example.myapplication.data.PostDatabase
import com.example.myapplication.data.PostRepository
import kotlinx.coroutines.*

class PostCacheHandler(application: Application, ctx: Context) {
    val ctx = ctx
    val postDao = PostDatabase.getDatabase(application).postDao()
    val repository = PostRepository(postDao)

    public fun addPostToCache(post: Post) {
        val scope = CoroutineScope(newSingleThreadContext("name"))

        scope.launch(Dispatchers.IO) {
            repository.addPost(post)
            scope.cancel()
        }
    }

    public fun addPostsToCache(posts: List<Post>) {
        val scope = CoroutineScope(newSingleThreadContext("name"))

        scope.launch(Dispatchers.IO) {
            repository.addPosts(posts)
            scope.cancel()
        }
    }

    public fun getCachedPosts(callback: (List<com.example.myapplication.data.DBPost>) -> Unit) {
        var readAllData : LiveData<List<DBPost>> = repository.readAllData

        readAllData.observe(ctx as LifecycleOwner, Observer { posts ->
            callback(posts)
        })
    }

    public fun clearPostCache() {
        val scope = CoroutineScope(newSingleThreadContext("name"))

        scope.launch(Dispatchers.IO) {
            postDao.nukeTable()
            scope.cancel()
        }
    }
}