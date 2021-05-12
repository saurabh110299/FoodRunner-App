package com.example.foodrunner.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnectivity(context:Context):Boolean{
        val connectivtyManager=context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
        val activeNetwork:NetworkInfo?=connectivtyManager.activeNetworkInfo

        if (activeNetwork?.isConnected!=null)
        {
            return activeNetwork.isConnected
        }else{
            return false
        }
    }
}