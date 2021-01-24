package com.priyam.squareboatapplication.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val code:Int?=null
    ) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(code:Int?,message: String, data: T? = null) : Resource<T>(data, message,code)
    class Loading<T> : Resource<T>()
}