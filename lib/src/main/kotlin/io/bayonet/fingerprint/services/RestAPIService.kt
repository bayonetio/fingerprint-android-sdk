package io.bayonet.fingerprint.services

import android.content.Context
import io.bayonet.fingerprint.core.domain.GetTokenResponse
import io.bayonet.fingerprint.core.domain.IRestAPI
import io.bayonet.fingerprint.lib.R
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

//object RestAPIConstants {
//    const val TABLE_USER_ATTRIBUTE_EMPID = "_id"
//    const val TABLE_USER_ATTRIBUTE_DATA = "data"
//}

const val REST_API_GET_TOKEN = "token"

class RestAPIService(
    private val ctx: Context,
    private val apiKey: String,
): IRestAPI {
    init {
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }
    }

    override fun getToken(): GetTokenResponse {
        val baseURL = ctx.applicationContext.getString(R.string.restapi_url)
        val url = URL("${baseURL}/${REST_API_GET_TOKEN}")
        val restApiHttpConnection = url.openConnection() as HttpURLConnection
        restApiHttpConnection.setRequestProperty("Authorization", "Bearer ${apiKey}")

        /*
        if (restApiHttpConnection.responseCode != HttpURLConnection.HTTP_OK) {
        }
        */

        var tokenResponse: GetTokenResponse
        restApiHttpConnection.inputStream.bufferedReader().use { textReader ->
            val jsonString: String = textReader.readText()
            tokenResponse = Json.decodeFromString<GetTokenResponse>(jsonString)
        }

        return tokenResponse
    }
}