package io.bayonet.fingerprint.services

import io.bayonet.fingerprint.core.domain.GetTokenResponse
import io.bayonet.fingerprint.core.domain.IRestAPI
import io.bayonet.fingerprint.core.domain.Token
import java.net.HttpURLConnection
import java.net.URL

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.jvm.Throws
import java.lang.Exception

data class RestAPIServiceParameters(
    val url: String,
    val apiKey: String,
)

/**
 * RestAPIService is the service that create the request for the RestAPI and process the response
 * to provide a fingerprint domain result.
 */
class RestAPIService(
    private val params: RestAPIServiceParameters
): IRestAPI {
    private val REST_API_GET_TOKEN_PATH: String = "token"
    private val REST_API_REFRESH_TOKEN_PATH: String = "refresh"

    init {
        require(params.apiKey.isNotBlank()) { "The api key cannot be empty" }
        require(params.url.isNotBlank()) { "The url cannot be empty" }

        try {
            URL(params.url)
        } catch (_: Exception) {
            throw IllegalArgumentException("The url is not valid")
        }
    }

    /**
     * getToken make a request to the RestAPI to generate a random token that is associated with the
     * device where the request is realized.
     *
     * @return a GetTokenResponse
     */
    @Throws(Exception::class)
    override fun getToken(): GetTokenResponse {
        val url = URL("${params.url}/${REST_API_GET_TOKEN_PATH}")
        val restApiHttpConnection = url.openConnection() as HttpURLConnection
        restApiHttpConnection.setRequestProperty("Authorization", "Bearer ${params.apiKey}")

        val unauthorizedRequestCodes = listOf<Int>(
            HttpURLConnection.HTTP_UNAUTHORIZED,
        )

        val error = when (restApiHttpConnection.responseCode) {
            // Unauthorized error
            in unauthorizedRequestCodes -> Exception("unauthorized")
            // Server errors
            in 500..599 -> Exception(restApiHttpConnection.responseMessage)
            // Request errors
            in 400..499 -> Exception("malformed request")
            // No error
            else -> null
        }

        if (error != null) {
            throw error
        }

        var tokenResponse: GetTokenResponse
        restApiHttpConnection.inputStream.bufferedReader().use { textReader ->
            val jsonString: String = textReader.readText()
            tokenResponse = Json.decodeFromString<GetTokenResponse>(jsonString)
        }

        return tokenResponse
    }

    /**
     * refresh update the token
     *
     * @return void
     */
    @Throws(Exception::class)
    override fun refresh(token: Token) {
        val url = URL("${params.url}/${REST_API_REFRESH_TOKEN_PATH}/${token.bayonetID}")
        val restApiHttpConnection = url.openConnection() as HttpURLConnection
        restApiHttpConnection.setRequestProperty("Authorization", "Bearer ${params.apiKey}")

        val unauthorizedRequestCodes = listOf<Int>(
            HttpURLConnection.HTTP_UNAUTHORIZED,
        )

        val error = when (restApiHttpConnection.responseCode) {
            // Unauthorized error
            in unauthorizedRequestCodes -> Exception("unauthorized")
            // Server errors
            in 500..599 -> Exception(restApiHttpConnection.responseMessage)
            // Request errors
            in 400..499 -> Exception("malformed request")
            // No error
            else -> null
        }

        if (error != null) {
            throw error
        }
    }
}
