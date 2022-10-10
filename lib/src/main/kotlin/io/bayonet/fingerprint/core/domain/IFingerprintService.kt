package io.bayonet.fingerprint.core.domain

interface IFingerprintService {
    suspend fun generateToken(): Fingerprint
    // fun refreshToken(fingerprintID: String)
}