package io.bayonet.fingerprint.services

import android.content.Context
import io.bayonet.fingerprint.core.domain.GetTokenResponse
import io.bayonet.fingerprint.core.domain.IRestAPI
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val REST_API_URL = "http://10.0.2.2:9000/v3"
const val REST_API_GET_TOKEN = "token"

/**
 * RestAPIService is the service that create the request for the RestAPI and process the response
 * to provide a fingerprint domain result.
 */
class RestAPIService(
    private val ctx: Context,
    private val apiKey: String,
): IRestAPI {
    init {
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }
    }

    /**
     * getToken make a request to the RestAPI to generate a random token that is associated with the
     * device where the request is realized.
     *
     * @return a GetTokenResponse
     */
    override fun getToken(): GetTokenResponse {
        val url = URL("${REST_API_URL}/${REST_API_GET_TOKEN}")
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