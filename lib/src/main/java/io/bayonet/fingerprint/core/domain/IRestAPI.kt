package io.bayonet.fingerprint.core.domain

interface IRestAPI {
    fun getToken(): GetTokenResponse
}