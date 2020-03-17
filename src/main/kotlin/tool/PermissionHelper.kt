
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.SparseArray

/*
 * @Author: zhuxiaoyao
 * @Date: 2018/12/21 - 14 : 01
 * @LastEditors: zhuxiaoyao
 * @LastEditTime: 2020-03-17 15:46:49
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */
class PermissionHelper(private val activity: BaseActivity) {

    private val requestMap: SparseArray<OnRequestListener> = SparseArray()

    interface Code {
        companion object {
            const val CAMERA = 1001
            const val WRITE = 1002
            const val READ = 1003
        }
    }

    interface Permission {
        companion object {
            const val CAMERA = Manifest.permission.CAMERA
            const val WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE
            const val READ = Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    interface OnGrantListener {
        fun onGranted()
    }

    interface OnDenyListener {
        fun onDenied()
    }

    private interface OnRequestListener {
        fun onGranted()

        fun onDenied()
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    // 0读权限 -1 相机 -2  写权限
    fun request(option: Int, onGrant: OnGrantListener, onDeny: OnDenyListener) {
        val listener = object : OnRequestListener {
            override fun onGranted() {
                onGrant.onGranted()
            }

            override fun onDenied() {
                onDeny.onDenied()
            }
        }

        val code = when (option) {
            -1 -> {
                Code.CAMERA
            }
            -2 -> {
                Code.WRITE
            }
            else -> {
                Code.READ
            }
        }

        val permission = when (option) {
            -1 -> {
                Permission.CAMERA
            }
            -2 -> {
                Permission.WRITE
            }
            else -> {
                Permission.READ
            }
        }

        requestMap.put(code, listener)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onGranted()
            return
        }

        if (hasPermission(permission)) {
            listener.onGranted()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                activity.runOnUiThread { showRationaleDialog(code, permission, option, listener) }
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), code)
            }
        }
    }

    private fun showRationaleDialog(code: Int, permission: String, option: Int, listener: OnRequestListener) {
        LogUtil.d("显示请求")
        val permissionsDialog = MessageDialog.instance(option)
        permissionsDialog.show(activity.supportFragmentManager, "permissionDialog")
        permissionsDialog.setClickListener(object : MessageDialog.MessageDialogListener {
            override fun cancel() {
                listener.onDenied()
            }

            override fun commit() {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), code)
            }
        })
    }

    fun handleResult(requestCode: Int, grantResults: IntArray) {
        val listener = requestMap.get(requestCode) ?: return
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listener.onGranted()
        } else {
            listener.onDenied()
        }
    }
}
