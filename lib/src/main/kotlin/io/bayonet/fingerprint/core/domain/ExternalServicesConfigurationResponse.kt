package io.bayonet.fingerprint.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Represents the response from the RestAPI when request a new token.
 *
 * @property bayonetID the token identifier in bayonet service.
 * @property environment the token environment in bayonet service.
 * @property services the configuration for external services.
 * @constructor Creates a RestAPI response.
 */
@Serializable
data class ExternalServicesConfigurationResponse(
    @JsonNames("fingerprintjs")
    val fingerprintjs: FingerprintJSServiceConfiguration,
)

/**
 * Represents the configuration required by the FingerprintJS service.
 *
 * @property apiKey is the key to use FingerprintJS service.
 * @constructor Creates a FingerprintJS service configuration.
 */
@Serializable
data class FingerprintJSServiceConfiguration(
    @JsonNames("apikey")
    val apiKey: String,
)
