import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext.Companion.getActor
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.CommUtils
import wasteservice.state.ServiceAreaState
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertTrue

class TestWasteServiceHandler {
    companion object{
        private var setup = false
        private lateinit var conn: Interaction2021
        @BeforeClass
        @JvmStatic
        fun startMockCtx(){
            println("TestContainerManager    |   launching mockCtx...")
            val mockCtxScope = CoroutineScope(CoroutineName("CtxScope"))
            mockCtxScope.launch {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val serverSocket    = aSocket(selectorManager).tcp().bind("127.0.0.1", 8056)
                val socket = serverSocket.accept()
                val receiveChannel = socket.openReadChannel()
                try {
                    val name = receiveChannel.readUTF8Line()
                } catch (e: Throwable) {
                    socket.close()
                }
            }
        }
    }
    @Before
    fun testSetup(){
        if(!setup){
            println("TestWasteServiceHandler    |   setup...")

            println("TestWasteServiceHandler    |   launching ctxwasteservice...")
            object : Thread(){
                override fun run() {
                    main()
                }
            }.start()

            var wasteservicehandler = getActor("wasteservicehandler")
            while (wasteservicehandler == null) {
                println("TestWasteServiceHandler    |   waiting for application start...")
                CommUtils.delay(200)
                wasteservicehandler = getActor("wasteservicehandler")
            }
            try {
                println("TestWasteServiceHandler    |   connection...")
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                println("TestWasteServiceHandler    |   connection failed...")
            }
            setup = true
        }
    }
    @Test
    fun testLoadAccept(){
        println("TestWasteServiceHandler    |   testLoadAccept...")
        var answ = ""
        var storeWaste = "msg(storewaste, request, testunit, wasteservicehandler, storewaste(PLASTIC, 300),1)"
        try {
            answ = conn.request(storeWaste)
        } catch (e: Exception) {
            println("TestWasteServiceHandler    |   request failed...")
        }
        assertTrue { answ.contains("loadaccept") }
    }
    @Test
    fun testLoadReject(){
        println("TestWasteServiceHandler    |   testLoadReject...")
        var answ = ""
        var storeWaste = "msg(storewaste, request, testunit, wasteservicehandler, storewaste(PLASTIC, 600),1)"
        try {
            answ = conn.request(storeWaste)
        } catch (e: Exception) {
            println("TestWasteServiceHandler    |   request failed...")
        }
        assertTrue { answ.contains("loadrejected") }
    }

}