
import android.app.Activity
import android.os.Binder
import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @Author: Hasiy
 * @Date: 2019/10/30 - 14 : 54
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

/**
 *  在 Activity 中使用：
 *   startActivity<SecondActivity>("id" to 9547,"name" to "WG")  // Anko
 *
 *  private val id: Int by bindExtra("id")
 *  private val name: String by bindExtra("name")
 *
 * 在 Fragment 中使用：
 * private val person: Person by bindArgument("person")
 */

fun <U, T : Any> Activity.bindExtra(key: String) = BindLoader<U, T>(key)

fun <U, T : Any> Fragment.bindArgument(key: String) = BindLoader<U, T>(key)

private class IntentDelegateWrite<in U, T : Any>(private val key: String) : ReadWriteProperty<U, T> {
    override fun setValue(thisRef: U, property: KProperty<*>, value: T) {
        val key = property.name
        when (thisRef) {
            is Fragment -> {
                if (thisRef.arguments == null) thisRef.arguments = Bundle()
                val args = thisRef.arguments
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
            is Activity -> {
                val intent = thisRef.intent
                when (value) {
                    is Boolean -> intent!!.putExtra(key, value)
                    is String -> intent!!.putExtra(key, value)
                    is Int -> intent!!.putExtra(key, value)
                    is Short -> intent!!.putExtra(key, value)
                    is Long -> intent!!.putExtra(key, value)
                    is Byte -> intent!!.putExtra(key, value)
                    is ByteArray -> intent!!.putExtra(key, value)
                    is Char -> intent!!.putExtra(key, value)
                    is CharArray -> intent!!.putExtra(key, value)
                    is CharSequence -> intent!!.putExtra(key, value)
                    is Float -> intent!!.putExtra(key, value)
                    is Bundle -> intent!!.putExtra(key, value)
                    is android.os.Parcelable -> intent!!.putExtra(key, value)
                    is java.io.Serializable -> intent!!.putExtra(key, value)
                    else -> throw IllegalStateException("Type ${value.javaClass.canonicalName} of property ${property.name} is not supported")
                }
            }
        }
    }

    override fun getValue(thisRef: U, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        return when (thisRef) {
            is Fragment -> thisRef.arguments?.get(key) as T
            else -> (thisRef as Activity).intent?.extras?.get(key) as T
        }
    }
}

class BindLoader<in U, T : Any>(private val key: String) {
    operator fun provideDelegate(thisRef: U, prop: KProperty<*>): ReadWriteProperty<U, T> {
        return IntentDelegateWrite(key)
    }
}
