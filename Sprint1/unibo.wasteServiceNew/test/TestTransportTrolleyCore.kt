/*
* ---------------------------------------------------------------------------------------------------------------------
* Per eseguire il test è necessario cambiare estensione del file testTransportTrolleyCore.qakx in .qak
* Eliminare tutti i package in "src" e rigenerarli salvando il file testTransportTrolleyCore.qak
* ---------------------------------------------------------------------------------------------------------------------
* */
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import sys.state.SystemState
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
class TestTransportTrolleyCore {

    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
        private lateinit var obs: TypedCoapTestObserver<SystemState>
    }

    @Before
    fun setUp(){
        if(!setup) {
            ColorsOut.outappl("TestTransportTrolleyCore 	|	setup...", ColorsOut.GREEN)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ws = QakContext.getActor("transporttrolleycore")
            while (ws == null) {
                ColorsOut.outappl("TestTransportTrolleyCore 	|	waiting for application starts...", ColorsOut.GREEN)
                CommUtils.delay(200)
                ws = QakContext.getActor("transporttrolleycore")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestTransportTrolleyCore 	|	TCP connection failed...", ColorsOut.GREEN)
            }
            startObs("localhost:8055")
            obs.getNext()
            setup = true
        } else {
            //obs.clearHistory()
        }
    }

    fun startObs(addr: String?) {
        val setupOk = ArrayBlockingQueue<Boolean>(1)

        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    SystemState.fromJsonString(it)
                }
                val ctx = "ctxwasteservice"
                val actor = "systemstatemanager"
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
    fun test1SingleRequest(){
        ColorsOut.outappl("TestTransportTrolleyCore 	|	test1Pickup...", ColorsOut.GREEN)

        var asw = ""
        val prevState =obs.currentTypedState!!
        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 50),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	 some err in request: $e", ColorsOut.MAGENTA)
        }

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS), newState.getCurrentBoxWeight(sys.state.Material.GLASS) )
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC)+50.0, newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))


        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(asw.contains("loadaccept"))

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("HOME", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

    }

    @Test
    fun test2MultipleRequest() {
        ColorsOut.outappl("TestTransportTrolleyCore 	|	test2MultipleRequest...", ColorsOut.GREEN)

        var asw = ""
        var prevState = obs.currentTypedState!!
        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 10),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	 some err in request: $e", ColorsOut.MAGENTA)
        }

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS), newState.getCurrentBoxWeight(sys.state.Material.GLASS))
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC)+10, newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(asw.contains("loadaccept"))

        prevState = obs.currentTypedState!!
        storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(GLASS, 100),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestTransportTrolleyCore	|	 some err in request: $e", ColorsOut.MAGENTA)
        }

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS) + 100, newState.getCurrentBoxWeight(sys.state.Material.GLASS))
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC), newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))

        newState = obs.getNext()
        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("PLASTICBOX", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("PICKINGUP", newState.getCurrState().toString())
        assertTrue(asw.contains("loadaccept"))

        newState = obs.getNext()
        assertEquals("INDOOR", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("GLASSBOX", newState.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("GLASSBOX", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("ONTHEROAD", newState.getCurrPosition().toString())
        assertEquals("MOVING", newState.getCurrState().toString())

        newState = obs.getNext()
        assertEquals("HOME", newState.getCurrPosition().toString())
        assertEquals("IDLE", newState.getCurrState().toString())

    }


}