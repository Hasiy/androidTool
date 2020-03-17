import java.io.Serializable

/**
 * @Author: zhuxiaoyao
 * @Date: 2019/1/7 - 13 : 32
 * @Description: Base
 * @LastEditTime: 2020-03-17 15:31:24
 * @Email: hasiy.jj@gmail.com
 */

private val const HTTP_SUCCESS=200

open class BaseCode : Serializable {
    var code: Int = 0
    var error: String? = null
}

fun BaseCode.handling(success: () -> Unit, failure: () -> Unit) {
    when (code == HTTP_SUCCESS) {
        true -> {
            success()
        }
        false -> {
            failure()
        }
    }
}

fun BaseCode.handling(success: () -> Unit, failure: String) {
    when (code == HTTP_SUCCESS) {
        true -> {
            success()
        }
        false -> {
            LogUtil.toastWarning(failure + error)
        }
    }
}

fun BaseCode.handling(success: () -> Unit) {
    when (code == HTTP_SUCCESS) {
        true -> {
            success()
        }
        false -> {
            error?.let { LogUtil.toastWarning(it) }
        }
    }
}
