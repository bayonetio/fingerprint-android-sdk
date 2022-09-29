package io.bayonet.fingerprint.services.android

import io.bayonet.fingerprint.core.domain.IExternalService

class FingerprintJSService: IExternalService {
    override fun run() {
        println("go to fingerprintjs")
    }
}