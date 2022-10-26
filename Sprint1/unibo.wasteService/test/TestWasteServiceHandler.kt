import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext.Companion.getActor
import kotlinx.coroutines.*
import mockctx.mockCtx
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
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
            ColorsOut.outappl("TestWasteServiceHandler   |   launching mockCtx...", ColorsOut.MAGENTA)
            mockCtx("transporttrolley", 8056)
        }
    }
    @Before
    fun testSetup(){
        if(!setup){
            ColorsOut.outappl("TestWasteServiceHandler   |   setup...", ColorsOut.MAGENTA)

            object : Thread(){
                override fun run() {
                    main()
                }
            }.start()

            var wasteservicehandler = getActor("wasteservicehandler")
            while (wasteservicehandler == null) {
                ColorsOut.outappl("TestWasteServiceHandler   |   waiting for application start...", ColorsOut.MAGENTA)

                CommUtils.delay(200)
                wasteservicehandler = getActor("wasteservicehandler")
            }
            try {
                ColorsOut.outappl("TestWasteServiceHandler   |   connection...", ColorsOut.MAGENTA)

                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestWasteServiceHandler   |   connection failed", ColorsOut.MAGENTA)
            }
            setup = true
        }
    }
    @Test
    fun testLoadAccept(){
        ColorsOut.outappl("TestWasteServiceHandler   |   testLoadAccept", ColorsOut.MAGENTA)

        var answ = ""
        var storeWaste = "msg(storewaste, request, testunit, wasteservicehandler, storewaste(PLASTIC, 300),1)"
        try {
            answ = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteServiceHandler   |   request failed...", ColorsOut.MAGENTA)
        }
        assertTrue { answ.contains("loadaccept") }
    }
    @Test
    fun testLoadReject(){
        ColorsOut.outappl("TestWasteServiceHandler   |   testLoadRejected", ColorsOut.MAGENTA)

        var answ = ""
        var storeWaste = "msg(storewaste, request, testunit, wasteservicehandler, storewaste(PLASTIC, 600),1)"
        try {
            answ = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteServiceHandler   |   request failed...", ColorsOut.MAGENTA)
        }
        assertTrue { answ.contains("loadrejected") }
    }

}