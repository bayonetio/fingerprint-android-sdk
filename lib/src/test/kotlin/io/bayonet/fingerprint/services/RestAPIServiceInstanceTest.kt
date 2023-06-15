package io.bayonet.fingerprint.services

import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Assertions.*

internal class RestAPIServiceInstanceTest {
    @TestFactory
    fun `test create a RestAPIService with incomplete parameters`(): Collection<DynamicTest> = listOf<RestAPIServiceParameters>(
            RestAPIServiceParameters("", ""),
            RestAPIServiceParameters("", "someapikey"),
            RestAPIServiceParameters("invalid url", "someapikey"),
            RestAPIServiceParameters("http://localhost", ""),
        ).map {
            dynamicTest("Check the parameters (${it.url}, ${it.apiKey})") {
                assertThrows(IllegalArgumentException::class.java) {
                   RestAPIService(it)
                }
            }
        }

    //@TestFactory
    //fun `test handle responses`(): Collection<DynamicTest> = listOf<RestAPIService>(
    //    RestAPIService(RestAPIServiceParameters("http://localhost", "validapikey")),
    //).map{
    //    val restAPIMockServer = okHttpMockServer(ServerConfiguration.custom {
    //        host = "localhost"
    //    }, {
    //        mock("/token") {
    //            body("its ok")
    //        }
    //    })
    //    restAPIMockServer.run {  }
    //    dynamicTest("Check restapiservice ${it}") {
    //        assertThrows(IllegalArgumentException::class.java) {
    //            it.getToken()
    //        }
    //    }
    //}
}