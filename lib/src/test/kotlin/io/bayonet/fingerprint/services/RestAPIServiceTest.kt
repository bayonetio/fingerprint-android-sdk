package io.bayonet.fingerprint.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.*

import io.github.infeez.kotlinmockserver.dsl.http.mock
import io.github.infeez.kotlinmockserver.dsl.http.okhttp.okHttpMockServer
import io.github.infeez.kotlinmockserver.matcher.and
import io.github.infeez.kotlinmockserver.server.ServerConfiguration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RestAPIServiceTest {
    private val localHost = "localhost"
    private val localPort = 9000

    private val restAPIMockServer = okHttpMockServer(ServerConfiguration.custom {
        host = localHost
        port = localPort
    })

    init {
        val serverErrorMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer error")
            }
        } on {
            code(HttpURLConnection.HTTP_INTERNAL_ERROR)
            emptyBody()
        }

        val malformedRequestMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer malformed")
            }
        } on {
            code(HttpURLConnection.HTTP_BAD_REQUEST)
            emptyBody()
        }

        val unauthorizedMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer noauthorizedapikey")
            }
        } on {
            code(HttpURLConnection.HTTP_UNAUTHORIZED)
            emptyBody()
        }

        val successNoEnvironmentMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer validnoenvironmentapikey")
            }
        } on {
            code(HttpURLConnection.HTTP_OK)
            body("""{"bayonet_id":"tokenid","services":{"fingerprintjs":{"apikey":"serviceapikey"}}}""")
        }

        val successEnvironmentMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer validenvironmentapikey")
            }
        } on {
            code(HttpURLConnection.HTTP_OK)
            body("""{"bayonet_id":"tokenid","environment":"sandbox","services":{"fingerprintjs":{"apikey":"serviceapikey"}}}""")
        }

        val successWithoutServicesMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer validwithoutservice")
            }
        } on {
            code(HttpURLConnection.HTTP_OK)
            body("""{"bayonet_id":"tokenid"}""")
        }

        val successWithInvalidResponseMock = mock {
            path {
                eq("/token")
            } and header("Authorization") {
                eq("Bearer validwithinvalidresponse")
            }
        } on {
            code(HttpURLConnection.HTTP_OK)
            body("content")
        }

        restAPIMockServer.addAll(
            serverErrorMock,
            malformedRequestMock,
            unauthorizedMock,
            successNoEnvironmentMock,
            successEnvironmentMock,
            successWithoutServicesMock,
            successWithInvalidResponseMock,
        )
    }

    @BeforeAll
    internal fun up() {
        restAPIMockServer.server.start()
    }

    @AfterAll
    internal fun down() {
        restAPIMockServer.server.stop()
    }

    @Test
    fun `test the connection with 5xx error`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "error")
        )

        assertThrows(Exception::class.java) {
            restAPIService.getToken()
        }
    }

    @Test
    fun `test the connection with unauthorized error`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "noauthorizedapikey")
        )

        assertThrows(Exception::class.java) {
            restAPIService.getToken()
        }
    }

    @Test
    fun `test the connection with malformed request`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "malformed")
        )

        assertThrows(Exception::class.java) {
            restAPIService.getToken()
        }
    }

    @Test
    fun `test successful without environment`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "validnoenvironmentapikey")
        )

        val token = restAPIService.getToken()
        assertEquals("tokenid", token.bayonetID)
        assertEquals("serviceapikey", token.services.fingerprintjs.apiKey)
        assertEquals(null, token.environment)
    }

    @Test
    fun `test successful with environment`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "validenvironmentapikey")
        )

        val token = restAPIService.getToken()
        assertEquals("tokenid", token.bayonetID)
        assertEquals("sandbox", token.environment)
        assertEquals("serviceapikey", token.services.fingerprintjs.apiKey)
    }

    @Test
    fun `test successful without services`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "validwithoutservice")
        )

        assertThrows(Exception::class.java) {
            restAPIService.getToken()
        }
    }

    @Test
    fun `test successful with invalid response content`() {
        val restAPIService = RestAPIService(
            RestAPIServiceParameters(
                "http://${localHost}:${localPort}",
                "validwithinvalidresponse")
        )

        assertThrows(Exception::class.java) {
            restAPIService.getToken()
        }
    }
}
