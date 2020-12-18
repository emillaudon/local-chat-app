package com.example.myapplication.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHandler {

    companion object {

        var values = arrayOf("cellular", "wifi", "ethernet")

        fun getType(context: Context): String? {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

//            TODO: Change return values to emun/res string variable or Int
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        println("Internet: NetworkCapabilities.TRANSPORT_CELLULAR")
                        return values[0]
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        println("Internet: NetworkCapabilities.TRANSPORT_WIFI")
                        return values[1]
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        println("Internet: NetworkCapabilities.TRANSPORT_ETHERNET")
                        return values[2]
                    }
                }
            }
            return null
        }

    }
}