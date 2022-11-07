package mockctx

import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import unibo.comm22.utils.ColorsOut

class MockCtxWorker (
        val clientSock: Socket,
        val ctxName: String,
        val scope: CoroutineScope,
        val onMessage: (ApplMessage) -> Unit = {}
        ) {

    private val readChannel = clientSock.openReadChannel()
    private var job : Job? = null

    fun start(){
        job = scope.launch {
            try {
                val port = clientSock.localAddress.toJavaAddress().port
                while(true) {
                    ColorsOut.outappl("mockCtxWorker[$ctxName]    |   waiting for messages on: $port...", ColorsOut.BLUE)
                    val name = readChannel.readUTF8Line()
                    try {
                        onMessage(ApplMessage(name!!))
                    } catch (e : Exception) {
                        ColorsOut.outappl("mockCtxWorker[$ctxName]    |   error: $e", ColorsOut.RED)
                    }
                    ColorsOut.outappl("mockCtxWorker[$ctxName]    |   received message $name on $port", ColorsOut.BLUE)
                }
            } catch (ce : CancellationException) {
                println("Error: $ce")
                clientSock.close()
            }
        }
    }

    fun stop() {
        job?.cancel()
    }

}

fun workerFor(clientSock: Socket, ctxName: String, scope: CoroutineScope, onMessage: (ApplMessage) -> Unit = {}) : MockCtxWorker =
        MockCtxWorker(clientSock, ctxName, scope, onMessage).apply { this.start() }