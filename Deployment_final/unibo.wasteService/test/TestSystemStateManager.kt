/*
* ---------------------------------------------------------------------------------------------------------------------
* Per eseguire il test è necessario cambiare estensione del file testSystemStateManager.qakx in .qak
* Eliminare tutti i package in "src" e rigenerarli salvando il file testSystemStateManager.qak
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
class TestSystemStateManager {

    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
        private lateinit var obs: TypedCoapTestObserver<SystemState>
    }

    @Before
    fun setUp(){
        if(!setup) {
            ColorsOut.outappl("Sprint2TestSystemStataManager	|	setup...", ColorsOut.GREEN)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ws = QakContext.getActor("wasteservice")
            while (ws == null) {
                ColorsOut.outappl("Sprint2TestSystemStataManager	|	waiting for application starts...", ColorsOut.GREEN)
                CommUtils.delay(200)
                ws = QakContext.getActor("wasteservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("Sprint2TestSystemStataManager	|	TCP connection failed...", ColorsOut.GREEN)
            }
            startObs("localhost:8055")
            obs.getNext()
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
    fun testLoadAcceptUpdate(){
        ColorsOut.outappl("Sprint2TestSystemStataManager	|	testLoadAcceptUpdate", ColorsOut.GREEN)

        var asw = ""
        val prevState = obs.currentTypedState!!

        ColorsOut.outappl("Starting state of the system: $prevState", ColorsOut.GREEN)

        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(GLASS, 50),1)"
        ColorsOut.outappl("Sending message to WasteService: $storeWaste", ColorsOut.GREEN)

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("Sprint2TestSystemStataManager	|	 some err in request: $e", ColorsOut.GREEN)
        }

        var newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS)+50, newState.getCurrentBoxWeight(sys.state.Material.GLASS) )
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC), newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))
        assertTrue(newState.getCurrPosition().toString().contains("HOME"))
        assertTrue(newState.getCurrState().toString().contains("IDLE"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("ONTHEROAD"))
        assertTrue(newState.getCurrState().toString().contains("MOVING"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("INDOOR"))
        assertTrue(newState.getCurrState().toString().contains("PICKINGUP"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("INDOOR"))
        assertTrue(newState.getCurrState().toString().contains("IDLE"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("ONTHEROAD"))
        assertTrue(newState.getCurrState().toString().contains("MOVING"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("GLASSBOX"))
        assertTrue(newState.getCurrState().toString().contains("DROPPINGOUT"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("GLASSBOX"))
        assertTrue(newState.getCurrState().toString().contains("IDLE"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("ONTHEROAD"))
        assertTrue(newState.getCurrState().toString().contains("MOVING"))

        newState = obs.getNext()
        ColorsOut.outappl("State Updated: $newState", ColorsOut.GREEN)
        assertTrue(newState.getCurrPosition().toString().contains("HOME"))
        assertTrue(newState.getCurrState().toString().contains("IDLE"))
    }
    fun testLoadRejectUpdate(){
        ColorsOut.outappl("Sprint2TestSystemStataManager	|	testLoadRejectUpdate", ColorsOut.GREEN)

        var asw = ""
        val prevState = obs.currentTypedState!!

        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 600),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("Sprint2TestSystemStataManager	|	 some err in request: $e", ColorsOut.GREEN)
        }

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS), newState.getCurrentBoxWeight(sys.state.Material.GLASS) )
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC), newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))
        assertTrue(newState.getCurrPosition().toString().contains("HOME"))
        assertTrue(newState.getCurrState().toString().contains("IDLE"))
    }
}