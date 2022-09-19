package unibo.testwasteservice

import org.eclipse.californium.core.CoapResponse
import java.util.concurrent.ArrayBlockingQueue

class TypedCoapTestObserver<T> (
        private val mapper : (String) -> T
        ): CoapTestObserver(){

    var currentTypedState : T? = null
    private set

    private val stateHistory = ArrayBlockingQueue<T>(10)

    override fun onLoad(response: CoapResponse) {
        super.onLoad(response)
        if(currState == null)
            currentTypedState = null
        else{
            currentTypedState = mapper(currState!!)
            stateHistory.put(currentTypedState)
        }


    }

    fun getNext() : T{
       return stateHistory.take()
    }

    fun clearHistory() {
        stateHistory.clear()
    }
}