package io.bayonet.fingerprint.services.android

import android.content.Context
import com.fingerprintjs.android.fpjs_pro.FingerprintJS
import com.fingerprintjs.android.fpjs_pro.FingerprintJSProResponse
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
import com.fingerprintjs.android.fpjs_pro.Configuration

import io.bayonet.fingerprint.core.domain.Token
import io.bayonet.fingerprint.core.domain.FingerprintJSServiceConfiguration
import io.bayonet.fingerprint.core.domain.IExternalService
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class AndroidFingerprintJSService(
    private val ctx: Context,
    private val config: FingerprintJSServiceConfiguration,
    private val token: Token,
): IExternalService {
    protected lateinit var fingerprintjsClient:  FingerprintJS;

    init {
        // Prepare the fingerprintJS client
        val factory = FingerprintJSFactory(ctx)
        val fingerprintjsConfig = Configuration(apiKey = this.config.apiKey)
        this.fingerprintjsClient = factory.createInstance(fingerprintjsConfig)
    }

    override fun analyze(): Unit {
        // Set the token for the FingerprintJS analysis
        val tags = mutableMapOf("browserToken" to this.token.bayonetID)
        if (this.token.environment != null) {
            tags["environment"] = token.environment as String
        }

        // Send the analysis to the service
        this.fingerprintjsClient.getVisitorId(
            tags,
            listener = { result ->
                println("OK ${result}")
            },
            errorListener = { error ->
                println("ERROR ${error}")
            }
        )
    }
}
