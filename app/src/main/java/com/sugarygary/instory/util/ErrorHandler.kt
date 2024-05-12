package com.sugarygary.instory.util

import com.squareup.moshi.Moshi
import com.sugarygary.instory.data.remote.response.ErrorResponseJsonAdapter
import retrofit2.HttpException

private val moshi = Moshi.Builder().build()
fun HttpException.handleError(): String {
    val adapter = ErrorResponseJsonAdapter(moshi)
    val jsonInString = response()?.errorBody()?.string()
    val errorBody = jsonInString?.let { adapter.fromJson(it) }
    return errorBody?.message ?: ""
}