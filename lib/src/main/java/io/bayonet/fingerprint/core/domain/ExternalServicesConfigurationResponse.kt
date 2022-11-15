package io.bayonet.fingerprint.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Represents configuration for external services used by the fingerprint.
 *
 * @property fingerprintjs is the configuration for the FingeprintJS service.
 * @constructor creates the external services from a RestAPI response.
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
