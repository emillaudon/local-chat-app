package com.example.myapplication.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.net.URL

object IconHandler {

    private lateinit var db: FirebaseFirestore
    private lateinit var memoryCache: LruCache<String, Bitmap>

    fun createCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    fun cacheIconsFromDb(callback: () -> Unit) {

        if (!this::memoryCache.isInitialized) {
            return
        }

        db = FirebaseFirestore.getInstance()

        db.collection("icons").get().addOnSuccessListener { snapshot ->

            if (snapshot.isEmpty || snapshot.count() != 2) {
                return@addOnSuccessListener
            }

            val docs = snapshot.documents

            val thread = Thread(Runnable {

                val iconUrl = URL(docs[0].data!!["url"].toString())
                val iconName = docs[0].id

                val imgStream = iconUrl.openStream()
                val image = BitmapFactory.decodeStream(imgStream)

                println("xyz" + image + iconName)

                memoryCache.put(iconName, image)


                val iconUrl2 = URL(docs[1].data!!["url"].toString())
                val iconName2 = docs[1].id

                val imgStream2 = iconUrl2.openStream()
                val image2 = BitmapFactory.decodeStream(imgStream2)

                println("xyz" + image2 + iconName2)

                memoryCache.put(iconName2, image2)

                callback()
            })
            thread.start()


        }

    }


    fun get(icon: String): Bitmap?  {

        if (!this::memoryCache.isInitialized) {
            return null
        }

        println("xyz get: " + memoryCache.snapshot().values)

        var bitmap: Bitmap? = null

        when(icon) {
            "snow"  -> bitmap = memoryCache.get("snow")
            "sun"   -> bitmap = memoryCache.get("sun")
        }

//        println("xyz get: " + bitmap.toString())



        return bitmap
    }



    fun isCached(): Boolean {

        if (memoryCache.snapshot().values.count() == 0) return false

        return true
    }



}