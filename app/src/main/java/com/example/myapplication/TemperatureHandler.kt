package com.example.myapplication

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

@RequiresApi(Build.VERSION_CODES.N)
class TemperatureHandler(location: Location) {

    private val long = location.longitude.toString().take(9)
    private val lat = location.latitude.toString().take(9)

    private val apiUrl = URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/" +
            long + "/lat/" + lat + "/data.json")


    fun getTemperature(callback: (Double) -> Unit) {

        val thread = Thread(Runnable {

            try {
//              TODO: Annoying json iteration has to be better way
                val jsonObject = JSONObject(apiUrl.readText())
                val dataItem = jsonObject["timeSeries"] as JSONArray
                val jsonIterationObj1 = dataItem[1] as JSONObject
                val jsonIterationObj2 = jsonIterationObj1["parameters"] as JSONArray
                val result = jsonIterationObj2[1] as JSONObject
                val i = result["values"] as JSONArray

                val temperature = i[0] as Double

                callback(temperature)
            }
            catch (e:Exception) {
//                TODO: handle error
                println(e)
            }
        })
        thread.start()
    }



}