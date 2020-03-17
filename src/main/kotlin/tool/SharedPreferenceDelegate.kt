
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
 * @Author: Hasiy
 * @Date: 2020/1/14 - 10 : 12 Z
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

class SharedPreferenceDelegate<T>(
    private val context: Context,
    private val name: String,
    private val default: T,
    private val prefName: String = "default"
) : ReadWriteProperty<Any?, T> {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        println("setValue from delegate")
        return getPreference(key = name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        println("setValue from delegate")
        putPreference(key = name, value = value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPreference(key: String): T {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        return when (default) {
            is String -> prefs.getString(key, default)
            is Long -> prefs.getLong(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is Float -> prefs.getFloat(key, default)
            is Int -> prefs.getInt(key, default)
            else -> throw IllegalArgumentException("Unknown Type.")
        } as T
    }

    private fun putPreference(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            else -> throw IllegalArgumentException("Unknown Type.")
        }
    }.apply()
}
