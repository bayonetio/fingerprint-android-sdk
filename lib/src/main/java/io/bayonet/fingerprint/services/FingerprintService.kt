package io.bayonet.fingerprint.services

import android.content.Context
import java.io.IOException
import kotlin.jvm.Throws

import io.bayonet.fingerprint.core.domain.*

import io.bayonet.fingerprint.services.android.AndroidFingerprintJSService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * FingerprintService is the service to manage the device's fingerprint.
 *
 * @property ctx is the android context
 * @property apiKey is the fingerprint Bayonet api key
 */
class FingerprintService(
    private val ctx: Context,
    private val apiKey: String,
): IFingerprintService {
    protected lateinit var restAPIService: IRestAPI;

    init {
        // Validate the parameters
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }

        // Initialize the RestAPI Service
        this.restAPIService = RestAPIService(ctx, apiKey)
    }

    @Throws(IOException::class, InterruptedException::class)
    override suspend fun analyze(): Token {
        // Generate token from the RestAPI
        val tokenResponse: GetTokenResponse = this.restAPIService.getToken()

        // Create components from the response
        // Build the token
        val token = Token(tokenResponse.bayonetID, tokenResponse.environment)

        // Build the services configuration
        val fingerprintjsServiceConfiguration = FingerprintJSServiceConfiguration(tokenResponse.services.fingerprintjs.apiKey)

        // Initialize the FingerprintJS service
        val fingerprintjsService = AndroidFingerprintJSService(ctx, fingerprintjsServiceConfiguration, token)

        // Analyze with the FingerprintJS service
        fingerprintjsService.analyze()

        return token
    }
}
