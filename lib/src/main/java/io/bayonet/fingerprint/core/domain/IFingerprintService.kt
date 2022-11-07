package io.bayonet.fingerprint.core.domain

interface IFingerprintService {
    suspend fun analyze(): Token
    // fun refreshToken(fingerprintID: String)
}