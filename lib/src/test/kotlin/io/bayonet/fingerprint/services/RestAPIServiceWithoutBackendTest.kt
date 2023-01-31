package io.bayonet.fingerprint.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.ConnectException

internal class RestAPIServiceWithoutBackendTest {
    @Test
    fun `test generate token cannot connect to the restapi`() {
        val restAPIService = RestAPIService(RestAPIServiceParameters("http://localhost", "validapikey"))

        assertThrows(ConnectException::class.java) {
            restAPIService.getToken()
        }
    }
}