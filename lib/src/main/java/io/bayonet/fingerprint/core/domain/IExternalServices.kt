package io.bayonet.fingerprint.core.domain

/**
 * IExternalServices is the interfaz that all external services used by Fingerprint have to
 * have to implements.
 */
interface IExternalService {
    fun analyze(): Unit
}