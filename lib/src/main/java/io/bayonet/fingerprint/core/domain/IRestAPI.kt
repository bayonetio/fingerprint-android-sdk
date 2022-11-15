package io.bayonet.fingerprint.core.domain

/**
 * IRestAPI is the interface that a RestAPI service has to implement.
 */
interface IRestAPI {
    fun getToken(): GetTokenResponse
}