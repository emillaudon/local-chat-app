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

    var icons = HashMap<String, Bitmap>()

//    var icons = Int[]


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

                    when(iconName) {
                        "sun"   -> memoryCache.put("sun_icon", image)
                        "snow"  -> memoryCache.put("snow_icon", image)
                    }
                })
                thread.start()

            }

        }

    }


    fun save() {

        val imgUrlString = "https://firebasestorage.googleapis.com/v0/b/localchatapp-12747.appspot.com/o/snow_icon.png?alt=media&token=ab1231cf-877c-45cf-8fab-619500845e41"
        val imgUrl = URL(imgUrlString)

        val thread = Thread(Runnable {
            try {
                val imgStream = imgUrl.openStream()
                val image = BitmapFactory.decodeStream(imgStream)

                println("xyz1: " + image.toString())

                memoryCache.put("snow_icon", image)



            }
            catch (e: Exception) {

            }
        })
        thread.start()
    }

    fun get(icon: String): Bitmap?  {

        if (!this::memoryCache.isInitialized) {
            return null
        }

        var bitmap: Bitmap? = null

        when(icon) {
            "snow"  -> bitmap = memoryCache.get("snow_icon")
            "sun"   -> bitmap = memoryCache.get("sun_icon")
        }

        println("xyz get: " + memoryCache.snapshot().values)
        println("xyz get: " + bitmap.toString())

        return bitmap
    }

    fun isCached(): Boolean {

        return false
    }



}