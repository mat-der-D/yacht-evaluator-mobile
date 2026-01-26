package com.example.yachtevaluator.data.error

import android.content.Context
import com.example.yachtevaluator.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val context: Context
) {
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> context.getString(R.string.error_bad_request)
                    500, 502, 503, 504 -> context.getString(R.string.error_server)
                    else -> context.getString(R.string.error_evaluation_failed)
                }
            }
            is SocketTimeoutException -> {
                context.getString(R.string.error_timeout)
            }
            is UnknownHostException, is IOException -> {
                context.getString(R.string.error_network)
            }
            else -> {
                context.getString(R.string.error_unknown)
            }
        }
    }
}
