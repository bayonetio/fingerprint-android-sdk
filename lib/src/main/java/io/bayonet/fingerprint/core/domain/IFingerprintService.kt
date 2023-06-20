package io.bayonet.fingerprint.core.domain

/**
 * IFingerprintService is the interface that the Fingerprint Service has to implement
 */
interface IFingerprintService {
    fun analyze(listener: (Token) -> Unit, errorListener: (Error) -> Unit)
}
