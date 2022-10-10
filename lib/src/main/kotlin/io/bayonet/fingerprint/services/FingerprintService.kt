package io.bayonet.fingerprint.services

import android.content.Context
import androidx.core.content.ContextCompat
import java.net.URL
import java.net.HttpURLConnection
import java.io.IOException
// import java.net.ConnectException

// import kotlinx.coroutines.newSingleThreadContext
// import kotlinx.coroutines.*
// import kotlinx.coroutines.GlobalScope
import kotlin.jvm.Throws
// import java.security.AccessController.getContext

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
import com.fingerprintjs.android.fpjs_pro.FingerprintJSProResponse
import com.fingerprintjs.android.fpjs_pro.Error

import io.bayonet.fingerprint.core.domain.Fingerprint
import io.bayonet.fingerprint.core.domain.IExternalService
import io.bayonet.fingerprint.core.domain.IFingerprintService
import io.bayonet.fingerprint.lib.R

// import kotlinx.coroutines.currentCoroutineContext

class FingerprintService(
    private val apiKey: String,
    private val ctx: Context,
    val externalServices: ArrayList<IExternalService> = ArrayList<IExternalService>()): IFingerprintService {
    init {
        require(apiKey.isNotBlank()) { "The api key cannot be empty" }
    }

    @Throws(IOException::class, InterruptedException::class)
    override suspend fun generateToken(): Fingerprint {
        // Generate the token from the backend
        val url = URL(ctx.applicationContext.getString(R.string.restapi_url))
        val restApiHttpConnection = url.openConnection() as HttpURLConnection
        restApiHttpConnection.setRequestProperty("Authorization", "Bearer $apiKey")
        var fingerprint: Fingerprint
        restApiHttpConnection.inputStream.bufferedReader().use { textReader ->
            val jsonString: String = textReader.readText()
            fingerprint = Json.decodeFromString<Fingerprint>(jsonString)
        }

        // Prepare the fingerprintJS service
        val factory = FingerprintJSFactory(ctx)
        val configuration = Configuration(apiKey = ctx.applicationContext.getString(R.string.fingerprintjs_api_key))

        val fpjsClient = factory.createInstance(
            configuration
        )

        // Store the fingerprintJS device
        val tags = mutableMapOf("browserToken" to fingerprint.bayonetID)
        if (fingerprint.environment != null) {
            tags["environment"] = fingerprint.environment as String
        }
        fpjsClient.getVisitorId(
            tags = tags,
            listener = { result: FingerprintJSProResponse ->
                // Handle ID
                println("ok $result")
            },
            errorListener = { err: Error ->
                println("error $err")
            },
        )

        return fingerprint
    }
}