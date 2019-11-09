package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.R
import java.lang.Exception

// Broadcast receiver to check for network changes
class NetworkStateChangedReceiver(private val view: View) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("StateChangedReceiver", "Detected yaw")
        if (!isConnectedToInternet(context)) {
            view.findViewById<TextView>(R.id.no_connection_textview).visibility = View.VISIBLE
        } else {
            view.findViewById<TextView>(R.id.no_connection_textview).visibility = View.INVISIBLE
        }

    }

    // check if the device is connected to the internet
    private fun isConnectedToInternet(context: Context?): Boolean {
        try {
            val connectivityManager =
                context!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)!!.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)!!.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            } else {
                connectivityManager.activeNetworkInfo!!.isConnected
            }
        } catch (exception: Exception) {
            Log.d("NetworkReceiver", "Error in isConnectedToInternet()")
            return false
        }

    }

}