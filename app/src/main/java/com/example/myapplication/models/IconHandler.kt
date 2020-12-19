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

    fun cacheIconsFromDb() {

        if (!this::memoryCache.isInitialized) {
            return
        }

        db = FirebaseFirestore.getInstance()

        db.collection("icons").get().addOnSuccessListener { snapshot ->

            if (snapshot.isEmpty) {
                return@addOnSuccessListener
            }

            for (doc in snapshot) {

                println(doc.data.toString())

                val thread = Thread(Runnable {

                    val iconUrl = URL(doc.data["url"].toString())
                    val iconName = doc.id

                    val imgStream = iconUrl.openStream()
                    val image = BitmapFactory.decodeStream(imgStream)

                    println("xyz" + image + iconName)

                    memoryCache.put(iconName, image)

//                    when(iconName) {
//                        "sun"   -> memoryCache.put("sun_icon", image)
//                        "snow"  -> memoryCache.put("snow_icon", image)
//                    }
                })
                thread.start()
            }

        }

    }


    fun get(icon: String): Bitmap?  {

        if (!this::memoryCache.isInitialized) {
            return null
        }

        println("xyz get: " + memoryCache.snapshot().values)

        var bitmap: Bitmap? = null

        when(icon) {
            "snow"  -> bitmap = memoryCache.get("snow_icon")
            "sun"   -> bitmap = memoryCache.get("sun_icon")
        }

//        println("xyz get: " + bitmap.toString())

        return bitmap
    }



    fun isCached(): Boolean {

        if (memoryCache.snapshot().values.count() == 0) return false

        return true
    }



}