package io.bayonet.fingerprint.mobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.bayonet.fingerprint.core.domain.Token
import io.bayonet.fingerprint.services.FingerprintService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: ExecutorCoroutineDispatcher = newSingleThreadContext("app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloTextView = findViewById<TextView>(R.id.text_component)

        runOnUiThread(Runnable {
            val apiKey = "12345678"
            val fingerprintService = FingerprintService(
                this,
                apiKey,
            )

            GlobalScope.launch {
                try {
                    val token: Token = fingerprintService.analyze()

                    // withContext(Dispatchers.Default) {
                    helloTextView.text = token.bayonetID
                    //}
                } catch (e: Exception) {
                    println("ERROR ${e}")
                }
            }
        })

        /*
        GlobalScope.launch {
            val fingerprint: Fingerprint = fingerprintService.generateToken()
            withContext(Dispatchers.Default) {
                // TODO("Update UI here!")
                // helloTextView.text = fingerprint.bayonetID
                println("moment to update view ${fingerprint.bayonetID}")
            }
            // TODO("Continue background processing...")
        }
         */

        /*
        runOnUiThread {
            // Stuff that updates the UI
            launch {
                try {


                    println("main activity ${fingerprint.bayonetID}")


                } catch (err: Exception) {
                    println(err.message)
                    helloTextView.text = err.message
                }
            }
        }
         */

        // val fingerprint = fingerprintService.generateToken()
        // println("main activicty ${fingerprint.bayonetID}")

    }
}