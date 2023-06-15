package io.bayonet.fingerprint.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Represents the response from the RestAPI when request a new token.
 *
 * @property bayonetID the token identifier in bayonet service.
 * @property environment the token environment in the fingerprint bayonet service.
 * @property services the configuration for external services.
 * @constructor Creates a RestAPI response.
 */
@Serializable
data class GetTokenResponse(
    @JsonNames("bayonet_id")
    val bayonetID: String,
    @JsonNames("environment")
    val environment: String? = null,
    @JsonNames("services")
    val services: ExternalServicesConfigurationResponse,
)
