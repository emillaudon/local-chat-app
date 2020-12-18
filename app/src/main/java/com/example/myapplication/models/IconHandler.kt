package com.example.myapplication.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.lang.Exception
import java.net.URL

object IconHandler {

    private lateinit var memoryCache: LruCache<String, Bitmap>


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

    fun get() {
        val img = memoryCache.get("snow_icon")



        println("xyz2: " + memoryCache.snapshot().values)
    }

    operator fun invoke() {
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


}