package io.bayonet.fingerprint.apps.cmd

import io.bayonet.fingerprint.services.FingerprintService

fun main() {
    val apiKey: String = "abcdefghijklmnopqrstuvwxyz"
    // val apiKey: String = "abcdefghijklmnopqrstuvwxy"
    val fingerprintService = FingerprintService(apiKey)

    val token = fingerprintService.generateToken()

    println("test")
    println(token.bayonetID)
}