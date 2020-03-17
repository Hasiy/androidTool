
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created on 2016/4/7.
 * By nesto
 */
internal object SharedPreferenceHelper {

    private val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(IDApplication.appContext.get())

    @JvmStatic
    fun putString(key: String, value: String) {
        LogUtil.d("SharedPreferences $key $value")
        preferences.edit().putString(key, value).apply()
    }

    @JvmStatic
    @JvmOverloads
    fun getString(key: String, defValue: String = ""): String {
        return preferences.getString(key, defValue)!!
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defValue)
    }
}
