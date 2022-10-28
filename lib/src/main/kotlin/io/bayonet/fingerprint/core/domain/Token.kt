package io.bayonet.fingerprint.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Represents the device's token.
 *
 * @property bayonetID the token identifier in bayonet service.
 * @property environment the token environment in bayonet service.
 * @constructor Creates a token.
 */
@Serializable
data class Token(
    @JsonNames("bayonet_id")
    val bayonetID: String,
    val environment: String?,
)
