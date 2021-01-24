package com.priyam.squareboatapplication.model;



import com.google.gson.annotations.SerializedName
import java.util.*

data class Icon (


    val is_premium: Boolean?,

    val is_icon_glyph: String?,

     val tags:ArrayList<String>,

    val published_at: String?,

    val icon_id: String?,

    val type: String?,

    var prices: ArrayList<IconPrice?>? ,

    var categories: ArrayList<IconCategory?>?,

    var styles: ArrayList<IconStyle?>? ,

    @SerializedName("vector_sizes")
var vector_Sizes: ArrayList<ImageVectorSize>?,

@SerializedName("raster_sizes")
var raster_Sizes: ArrayList<IconRasterSize>?,



)
