package uae.enbd.pixabay.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

fun Fragment.isNetworkAvailable(): Boolean {
    val connectivityManager =
        this.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}