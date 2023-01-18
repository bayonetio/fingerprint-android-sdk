package io.bayonet.fingerprint.core.domain

/**
 * IFingerprintService is the interface that the Fingerprint Service has to implement
 */
interface IFingerprintService {
    suspend fun analyze(): Token
    // fun refreshToken(fingerprintID: String)
}
