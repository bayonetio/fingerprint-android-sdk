package io.bayonet.fingerprint.services

import android.content.Context
import androidx.annotation.WorkerThread
import java.io.IOException
import kotlin.jvm.Throws
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

import io.bayonet.fingerprint.R
import io.bayonet.fingerprint.core.domain.*
import io.bayonet.fingerprint.services.android.AndroidFingerprintJSService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val STORE_KEY = "bayonet"
val STORE_TOKEN_KEY = "token"

val ENVIRONMENT_PRODUCTION_KEY = "production"
val ENVIRONMENT_STAGING_KEY = "staging"
val ENVIRONMENT_DEVELOP_KEY = "develop"

/**
 * FingerprintService is the service to manage the device's fingerprint.
 *
 * @property ctx is the android context
 * @property apiKey is the fingerprint Bayonet api key
 */
class FingerprintService(
    private val ctx: Context,
    private val apiKey: String,
    // Todo: change env to production
    private val BAYONET_ENVIRONMENT: String = ENVIRONMENT_DEVELOP_KEY
): IFingerprintService {
    // The RestAPI Service
    private var restAPIService: IRestAPI;

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        // Validate the parameters
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }

        // Initialize the RestAPI Service
        val restAPIServiceParameters = RestAPIServiceParameters(
            apiKey,
            BAYONET_ENVIRONMENT,
            ctx,
        )
        this.restAPIService = RestAPIService(restAPIServiceParameters)
    }

    /**
     * analyze fetch a token generated by the backend and then do the analysis
     * of the device fingerprint with the external services
     */
    @WorkerThread
    override fun analyze(
        listener: (Token) -> Unit,
        errorListener: (Error) -> Unit
    ) {
        executor.execute {
            var token: Token

            val storedToken = this.getStoreToken()

            // The token not exists
            if (storedToken == null) {
                // Generate token from the RestAPI
                try {
                    val tokenResponse = this.restAPIService.getToken()

                    token = Token(tokenResponse.bayonetID, tokenResponse.environment)

                    // Save the generated token
                    this.setStoreToken(token)

                    // Start third services
                    // Build the services configuration
                    val fingerprintjsServiceConfiguration = FingerprintJSServiceConfiguration(tokenResponse.services.fingerprintjs.apiKey)

                    // Initialize the FingerprintJS service
                    val fingerprintjsService = AndroidFingerprintJSService(ctx, fingerprintjsServiceConfiguration, token)

                    // Analyze with the FingerprintJS service
                    fingerprintjsService.analyze()

                    listener.invoke(token)
                } catch (err: Exception) {
                    errorListener.invoke(Error("there is an error '${err.message}'"))
                }
            } else {
                token = storedToken
                try {
                    restAPIService.refresh(token)
                } catch (err: Exception) {
                    println("error on refresh ${err}")
                }

                listener.invoke(token)
            }
        }
    }

    /**
     * getStoreToken load the token from shared preferences
     *
     * @returns a token or null
     */
    private fun getStoreToken(): Token? {
        var storedToken: Token? = null
        // Get the repository reference
        val sharedPreferences = ctx.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)

        // Read the stored json token
        val dataTokenStored: String? = sharedPreferences.getString(STORE_TOKEN_KEY, "{}")
        if (dataTokenStored != null) {
            try {
                // Parse the json string to a Token
                storedToken = Json.decodeFromString<Token>(dataTokenStored)
            } catch (error: Exception) {
                // println("Err${error}")
            }
        }

        return storedToken
    }

    /**
     * setStoreToken saves the token in shared preferences.
     *
     * @returns void
     */
    private fun setStoreToken(token: Token): Unit {
        // Get the repository reference
        val sharedPreferences = ctx.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)

        with (sharedPreferences.edit()) {
            putString(STORE_TOKEN_KEY, Json.encodeToString(token))
            apply()
        }
    }
}
