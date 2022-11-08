package unibo.coapobs

import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse

open class CoapTestObserver : CoapHandler {

        var currState: String? = null
            private set

        override fun onLoad(response: CoapResponse) {
            currState = response.responseText
        }

        override fun onError() {}
}