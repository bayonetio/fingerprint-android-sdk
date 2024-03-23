package io.bayonet.fingerprint.core.domain

/**
 * IExternalServices is the interface that all external services used by Fingerprint MUST implement.
 */
interface IExternalService {
    fun analyze(): Unit
}
