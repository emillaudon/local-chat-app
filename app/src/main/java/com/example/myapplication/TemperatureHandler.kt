package com.example.myapplication

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.math.roundToInt
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.N)
class TemperatureHandler(val location: Location) {

    private val long = location.longitude.toString().take(9)
    private val lat = location.latitude.toString().take(9)

    private val apiUrl = URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/" +
            long + "/lat/" + lat + "/data.json")


    fun getTemperature() {

//        with(apiUrl.openConnection() as HttpURLConnection) {
//
//            inputStream.bufferedReader().use {
//                it.lines().forEach { line ->
//                    println("zz" + line)
//                }
//            }
//        }

        println("zz" + location.latitude)

        val thread = Thread(Runnable {

            val jsonObject = JSONObject(apiUrl.readText())

            val dataItem = jsonObject["timeSeries"] as JSONArray



            println("zz" + dataItem[1])


        })
        thread.start()

    }



}