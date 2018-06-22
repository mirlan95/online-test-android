package com.example.mirlan.diplom.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by mirlan on 5/20/18.
 */
class NetworkConnection {
    companion object {
        @SuppressLint("MissingPermission")
            fun isNetworkConnected(context: Context): Boolean{
                val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectionManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }
}