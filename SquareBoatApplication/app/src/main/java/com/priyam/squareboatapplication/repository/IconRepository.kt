package com.priyam.squareboatapplication.repository

import com.priyam.squareboatapplication.network.IconFinderApi
import com.priyam.squareboatapplication.network.SafeApiRequest
import com.priyam.squareboatapplication.responseBody.IconResponseBody
import com.priyam.squareboatapplication.util.Constants
import com.priyam.squareboatapplication.util.Resource
import javax.inject.Inject

class IconRepository @Inject constructor(private val iconFinderApi: IconFinderApi,): SafeApiRequest() {
    suspend fun getIcon(count: Int, identifier: String): Resource<IconResponseBody>? {
        val headers = HashMap<String, String>()
        headers["Authorization"] = Constants.token

        return apiRequest { iconFinderApi.getIcon(headers,identifier,Constants.clientId, Constants.apiKey, count) }

    }

    suspend fun searchIcon( query: String): Resource<IconResponseBody>? {
        val headers = HashMap<String, String>()
        headers["Authorization"] = Constants.token

        return apiRequest { iconFinderApi.searchIcon(headers,query,Constants.clientId, Constants.apiKey) }

    }
}