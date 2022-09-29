package io.bayonet.fingerprint.core.domain

interface IFingerprintService {
    fun generateToken(): Fingerprint
    // fun refreshToken(fingerprintID: String)
}