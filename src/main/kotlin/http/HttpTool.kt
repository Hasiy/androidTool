package com.leqi.group.network

import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @Author: Hasiy
 * @Date: 2020/1/7 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

suspend fun <T> Call<T>.holdUp(): T {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            LogUtil.w("invokeOnCancellation: cancel the request.")
            LogUtil.toastWarning("请求异常，请检查网络后重试！")
            cancel()
        }
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                when (response.isSuccessful) {
                    true -> {
                        val body = response.body()
                        when (body == null) {
                            true -> {
                                val invocation = call.request().tag(Invocation::class.java)!!
                                val method = invocation.method()
                                val e = KotlinNullPointerException(
                                    "Response from " +
                                        method.declaringClass.name +
                                        '.' +
                                        method.name +
                                        " was null but response body type was declared as non-null"
                                )
                                LogUtil.w("KotlinNullPointerException：e$e || response = null ")
                                LogUtil.toastWarning("服务器异常，请稍候后重试！|| response = null")
                                continuation.resumeWithException(e)
                            }
                            false -> {
                                when (response.code() != HTTP_SUCCESS) {
                                    true -> {
                                        val invocation = call.request().tag(Invocation::class.java)!!
                                        val method = invocation.method()
                                        val e = KotlinNullPointerException(
                                            "Response from " +
                                                method.declaringClass.name +
                                                '.' +
                                                method.name +
                                                " was null but response body type was declared as non-null"
                                        )
                                        LogUtil.w("KotlinNullPointerException：e$e ||  code = ${response.code()}")
                                        LogUtil.toastWarning("服务器异常，请稍候后重试！ code = ${response.code()}")
                                        continuation.resumeWithException(e)
                                    }
                                    false -> {
                                        continuation.resume(body)
                                    }
                                }
                            }
                        }
                    }
                    false -> {
                        LogUtil.w("response.failure")
                        LogUtil.toastWarning("请求异常，请稍候后重试！|| response.failure")
                        continuation.resumeWithException(HttpException(response))
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                LogUtil.e("await.enqueue.onFailure::$t")
                LogUtil.toastError("请求异常，请稍候后重试！")
                continuation.resumeWithException(t)
                return
            }
        })
    }
}

fun <T> bean2RequestBody(bean: T): RequestBody {
    val objectString = Gson().toJson(bean)
    return RequestBody.create(MediaType.parse(Config.JSON_TYPE), objectString)
}
