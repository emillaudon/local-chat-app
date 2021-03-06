package com.example.myapplication.models

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


@RequiresApi(Build.VERSION_CODES.N)
class TemperatureHandler(private val activity: Activity) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private fun getLocation(callback: (Location) -> Unit) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

//        Ask for location permission
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                callback(location)
            }
            else {
                val thread = Thread(Runnable {

                    val locationRequest = LocationRequest.create()
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationRequest.interval = (20 * 1000)
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            for (location in locationResult.locations) {
                                if (location != null) {
                                    callback(location)
                                }
                            }
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                })
                thread.start()
            }
        }
    }

    fun getTemperature(callback: (Int?, Exception?) -> Unit) {

        getLocation { location ->

            if (this::locationCallback.isInitialized) {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }

            println("location:" + location)

            val thread = Thread(Runnable {
                try {

                    val long = location.longitude.toString().take(9)
                    val lat = location.latitude.toString().take(9)

//                    val long = 18.063240.toString()
//                    val lat = 59.334591.toString()


                    val apiUrl = URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/" +
                            long + "/lat/" + lat + "/data.json")

//                   TODO: Annoying json iteration has to be better way
                    val jsonObject = JSONObject(apiUrl.readText())
                    val dataItem = jsonObject["timeSeries"] as JSONArray
                    val jsonIterationObj1 = dataItem[1] as JSONObject
                    val parameters = jsonIterationObj1["parameters"] as JSONArray

                    var celKey = 0

                    for (i in 0..parameters.length() - 1) {

                        println("zzzz" + parameters[i])
                        val item = parameters[i] as JSONObject

                        if (item["unit"] == "Cel") {
                            celKey = i
                            break
                        }
                    }

                    val result = parameters[celKey] as JSONObject
                    val i = result["values"] as JSONArray

                    val temperature = i[0] as Double

                    println("temperature" + apiUrl)
                    println("temperature" + temperature)

                    callback(temperature.toInt(), null)
                }
                catch (e:Exception) {
//                TODO: handle error
                    println("Temperature error: " + e)

                    callback(null, e)
                }
            })
            thread.start()

        }


    }



}