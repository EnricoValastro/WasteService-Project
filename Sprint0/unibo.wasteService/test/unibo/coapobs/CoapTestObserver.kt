package unibo.coapobs

import org.eclipse.californium.core.CoapHandler
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ArrayBlockingQueue
import org.eclipse.californium.core.CoapResponse
import java.lang.InterruptedException
import kotlin.Throws

open class CoapTestObserver : CoapHandler {

    var currState: String? = null
        private set


    override fun onLoad(response: CoapResponse) {
        currState = response.responseText
    }

    override fun onError() {}

}