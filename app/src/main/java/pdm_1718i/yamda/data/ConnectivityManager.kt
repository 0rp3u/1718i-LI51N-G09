package pdm_1718i.yamda.data

import android.content.Context
import android.net.ConnectivityManager
import pdm_1718i.yamda.ui.App


object ConnectivityManager{
    private val manager by lazy { App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }


    fun isConnected(): Boolean{
        val networkInfo = manager.activeNetworkInfo
        if(networkInfo != null && networkInfo.isConnectedOrConnecting){
            return true
        }
        return false
    }

    fun isWifi(): Boolean{
        val networkInfo = manager.activeNetworkInfo
        if(networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI){
            return true
        }
        return false
    }
}