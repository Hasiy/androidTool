
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q

object NetWorkUtil {

    /**
     * 检测当的网络（WIFI、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    fun isNetworkAvailable(context: Context): Boolean {
        var hasNetwork = false
        val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @Suppress("DEPRECATION")
        when (SDK_INT < Q) {
            true -> {
                val info = mConnectivityManager.activeNetworkInfo
                if (info != null && info.isConnected) {
                    hasNetwork = true
                }
            }
            false -> {
                val networks = mConnectivityManager.allNetworks
                if (networks.isNotEmpty()) {
                    for (network in networks) {
                        val nc = mConnectivityManager.getNetworkCapabilities(network)
                        if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            hasNetwork = true
                        }
                    }
                }
            }
        }
        return hasNetwork
    }
}
