package com.priyam.squareboatapplication.network



import com.priyam.squareboatapplication.util.Resource
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            return if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = StringBuilder()
                if (errorBody!=null){
                    try {
                        message.append(JSONObject(errorBody).getString("message"))
                    } catch (jsonException: JSONException) {

                    }
                }
                else{
                    message.append(response.message())
                }
                message.append("${response.code()}")
                Resource.Error(response.code(),message.toString())
            }

        }
        catch (t:Throwable)
        {
            return when(t) {
                is IOException -> Resource.Error( null,"Network Failure")
                is SocketTimeoutException->Resource.Error(null,"Connection Timeout")
                else -> Resource.Error(null,"Something Went Wrong"+t.localizedMessage)
            }
        }
    }
}