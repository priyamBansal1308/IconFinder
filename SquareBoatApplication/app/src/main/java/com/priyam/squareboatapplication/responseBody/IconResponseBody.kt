package com.priyam.squareboatapplication.responseBody;


import com.google.gson.annotations.SerializedName;
import com.priyam.squareboatapplication.model.Icon
import com.priyam.squareboatapplication.model.Iconset;

data class IconResponseBody (

    @SerializedName("icons")
    val icons: List<Icon>?,

    @SerializedName("total_count")
    val total_count: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("code")
    val code: Int?


)
