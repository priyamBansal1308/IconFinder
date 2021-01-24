package com.priyam.squareboatapplication.repository

import com.priyam.squareboatapplication.responseBody.IconSetResponseBody
import com.priyam.squareboatapplication.network.IconFinderApi
import com.priyam.squareboatapplication.network.SafeApiRequest
import com.priyam.squareboatapplication.util.Constants
import com.priyam.squareboatapplication.util.Resource
import javax.inject.Inject

class IconSetRepository @Inject constructor(private val iconFinderApi: IconFinderApi,): SafeApiRequest() {
    suspend fun getIconSet(count: String, isPremium:Boolean): Resource<IconSetResponseBody>? {

        val headers = HashMap<String, String>()
        headers["Authorization"] = Constants.token

        return apiRequest { iconFinderApi.getIconsets(headers,Constants.clientId, Constants.apiKey, count,isPremium) }

    }
}