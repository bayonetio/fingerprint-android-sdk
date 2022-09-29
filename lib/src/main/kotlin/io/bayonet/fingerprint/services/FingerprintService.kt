package io.bayonet.fingerprint.services

import java.io.IOException
import java.net.URL
import java.net.http.HttpRequest
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import io.bayonet.fingerprint.core.domain.Fingerprint
import io.bayonet.fingerprint.core.domain.IExternalService
import io.bayonet.fingerprint.core.domain.IFingerprintService
import io.bayonet.fingerprint.services.android.FingerprintJSService

class FingerprintService(val apiKey: String): IFingerprintService {
    private val externalServices: ArrayList<IExternalService> = ArrayList<IExternalService>()

    init {
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }

        val fingerprintJSService: IExternalService = FingerprintJSService()
        externalServices.add(fingerprintJSService)
    }

    // throws java.io.IOException, java.lang.InterruptedException;
    override fun generateToken(): Fingerprint {
        // The fingerprint variable to populate
        var fingerprint: Fingerprint

        // Prepare the RestApi request
        val url = URL("http://localhost:9000/v3/token")
        val httpClient = HttpClient.newHttpClient()
        val authHeader: String = "Bearer ${this.apiKey}"
        val restApiRequest = HttpRequest.newBuilder()
            .uri(url.toURI())
            .header("Authorization", authHeader)
            .GET()
            .build()

        // Run the request
        try {
            val response: HttpResponse<String> = httpClient.send<String>(restApiRequest, BodyHandlers.ofString())
            fingerprint = Json.decodeFromString<Fingerprint>(response.body())
        } catch (ioError: IOException) {
            throw Exception("input-output error ${ioError.message}")
        } catch (iError: InterruptedException) {
            throw Exception("interrupted error ${iError.message}")
        } catch (err: Exception) {
            throw Exception("error ${err.message}")
        }

        // Request to the external services
        for (externalService in this.externalServices) {
            externalService.run()
        }

        return fingerprint
    }
}