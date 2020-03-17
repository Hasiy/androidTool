
import android.os.Binder
import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

/**
 * @Author: Hasiy
 * @Date: 2019/10/30 - 14 : 52
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

/**
 * Eases the Fragment.newInstance ceremony by marking the fragment's args with this delegate
 * Just write the property in newInstance and read it like any other property after the fragment has been created
 * Inspired by Adam Powell, he mentioned it during his IO/17 talk about Kotlin
 */

class FragmentArgumentDelegate<T : Any> : kotlin.properties.ReadWriteProperty<Fragment, T> {

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        var value: T? = null
        if (value == null) {
            val args = thisRef.arguments ?: throw IllegalStateException("Cannot read property ${property.name} if no arguments have been set")
            @Suppress("UNCHECKED_CAST")
            value = args.get(property.name) as T
        }
        return value
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if (thisRef.arguments == null) {
            thisRef.arguments = Bundle()
        }

        val args = thisRef.arguments
        val key = property.name

        when (value) {
            is Boolean -> args!!.putBoolean(key, value)
            is String -> args!!.putString(key, value)
            is Int -> args!!.putInt(key, value)
            is Short -> args!!.putShort(key, value)
            is Long -> args!!.putLong(key, value)
            is Byte -> args!!.putByte(key, value)
            is ByteArray -> args!!.putByteArray(key, value)
            is Char -> args!!.putChar(key, value)
            is CharArray -> args!!.putCharArray(key, value)
            is CharSequence -> args!!.putCharSequence(key, value)
            is Float -> args!!.putFloat(key, value)
            is Bundle -> args!!.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args!!, key, value)
            is android.os.Parcelable -> args!!.putParcelable(key, value)
            is java.io.Serializable -> args!!.putSerializable(key, value)
            else -> throw IllegalStateException("Type ${value.javaClass.canonicalName} of property ${property.name} is not supported")
        }
    }
}
