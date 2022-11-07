
import it.unibo.ctxtransporttrolley.main
import it.unibo.kactor.QakContext
import mockCtx.MockCtx
import mockCtx.mockCtx
import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.OrderWith
import transporttrolley.state.TransportTrolleyState
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.ColorsOut.MAGENTA
import unibo.comm22.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
class TestTransportTrolleyCore {

    companion object{

        private var setup = false
        private lateinit var conn: Interaction2021
        private lateinit var obs: TypedCoapTestObserver<TransportTrolleyState>

        @BeforeClass
        @JvmStatic
        fun startMockCtx(){
            ColorsOut.outappl("TestTransportTrolleyCore   |   launching mockCtx...", MAGENTA)
            mockCtx("basicrobot", 8020, ) {
                if(it.msgId() == "dopath"){
                    conn.forward("msg(dopathdone, reply, testunit, transporttrolleymover, dopathdone(OK) ,1)")
                }
            }
            mockCtx("wasteservice",8055)
        }
    }

    @Before
    fun setUp(){
        if(!setup) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	setup...", MAGENTA)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var tTCore = QakContext.getActor("transporttrolleycore")
            while (tTCore == null) {
                ColorsOut.outappl("TestTransportTrolleyCore	|	waiting for application starts...", MAGENTA)
                CommUtils.delay(200)
                tTCore = QakContext.getActor("transporttrolleycore")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8056, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestTransportTrolleyCore	|	TCP connection failed...", MAGENTA)
            }
            startObs("localhost:8056")
            setup = true
        } else {
            obs.clearHistory()
        }
    }

    fun startObs(addr: String?) {
        val setupOk = ArrayBlockingQueue<Boolean>(1)

        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    TransportTrolleyState.fromJsonString(it)
                }
                val ctx = "ctxtransporttrolley"
                val actor = "transporttrolleycore"
                val path = "$ctx/$actor"
                val coapConn = CoapConnection(addr, path)
                coapConn.observeResource(obs)
                try {
                    setupOk.put(true)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        setupOk.take()
    }

    @Test
    @Throws(InterruptedException::class)
    fun test1Pickup(){
        ColorsOut.outappl("TestTransportTrolleyCore	|	testPickup...", MAGENTA)

        var asw = ""

        var pickup = "msg(pickup, request, testunit, transporttrolleycore, pickup(_) ,1)"

        try {
            asw = conn.request(pickup)
        } catch (e: Exception) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	 some err in request: $e", MAGENTA)
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(asw.contains("pickupdone"))

        newState = obs.getNext()

    }
    @Test
    @Throws(InterruptedException::class)
    fun test2Dropout(){
        ColorsOut.outappl("TestTransportTrolleyCore	|	testDropout...", MAGENTA)

        var dropout = "msg(dropout, dispatch, testunit, transporttrolleycore, dropout(PLASTIC) ,1)"

        try{
            conn.forward(dropout)
        } catch (e: Exception) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	 some err in request: $e", MAGENTA)
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

    }
    @Test
    @Throws(InterruptedException::class)
    fun test3BackHome(){
        ColorsOut.outappl("TestTransportTrolleyCore	|	testBackHome...", MAGENTA)

        var gotohome = "msg(gotohome, dispatch, testunit, transporttrolleycore, gotohome(_) ,1)"

        try{
            conn.forward(gotohome)
        } catch (e: Exception) {
            println("TestTransportTrolleyCore	|	 some err in request: $e")
        }

        var newState = obs.getNext()

        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()

        assertEquals("HOME", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

    }

}