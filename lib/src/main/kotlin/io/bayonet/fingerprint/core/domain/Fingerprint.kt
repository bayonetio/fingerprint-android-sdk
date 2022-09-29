package io.bayonet.fingerprint.core.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Fingerprint(
    @JsonNames("bayonet_id")
    val bayonetID: String
)
