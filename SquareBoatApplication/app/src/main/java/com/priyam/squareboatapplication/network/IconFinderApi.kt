package com.priyam.squareboatapplication.network


import com.priyam.squareboatapplication.responseBody.IconResponseBody
import com.priyam.squareboatapplication.responseBody.IconSetResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface IconFinderApi {

    @GET("/v4/iconsets")
    suspend fun getIconsets(
        @HeaderMap headers: Map<String, String>,
        @Query("client_id") apiClientId: String?,
        @Query("client_secret") apiClientSecret: String?,
        @Query("count") count: String?,
        @Query("premium") premium: Boolean?

        ): Response<IconSetResponseBody>


    @GET("/v4/iconsets/{iconset_identifier}/icons")
    suspend  fun getIcon(
        @HeaderMap headers: Map<String, String>,
        @Path(value = "iconset_identifier", encoded = true) idendifier: String?,
        @Query("client_id") apiClientId: String?,
        @Query("client_secret") apiClientSearch: String?,
        @Query("count") count: Int?,

    ): Response<IconResponseBody>

    @GET("/v4/icons/search")
    suspend  fun searchIcon(
            @HeaderMap headers: Map<String, String>,
            @Query(value = "query", encoded = true) query: String?,
            @Query("client_id") apiClientId: String?,
            @Query("client_secret") apiClientSearch: String?,


            ): Response<IconResponseBody>

}

