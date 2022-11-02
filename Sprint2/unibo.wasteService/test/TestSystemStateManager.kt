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
            ColorsOut.outappl("TestSystemStateManager	|	setup...", ColorsOut.GREEN)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ws = QakContext.getActor("wasteservice")
            while (ws == null) {
                ColorsOut.outappl("TestSystemStateManager	|	waiting for application starts...", ColorsOut.GREEN)
                CommUtils.delay(200)
                ws = QakContext.getActor("wasteservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestSystemStateManager	|	TCP connection failed...", ColorsOut.GREEN)
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
        ColorsOut.outappl("TestSystemStateManager	|	testLoadAcceptUpdate", ColorsOut.GREEN)

        var asw = ""
        val prevState = obs.currentTypedState!!

        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(GLASS, 50),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestSystemStateManager	|	 some err in request: $e", ColorsOut.GREEN)
        }

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS)+50, newState.getCurrentBoxWeight(sys.state.Material.GLASS) )
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC), newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))
    }

    fun testLoadRejectUpdate(){
        ColorsOut.outappl("TestSystemStateManager	|	testLoadRejectUpdate", ColorsOut.GREEN)

        var asw = ""
        val prevState = obs.currentTypedState!!

        var storeWaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 600),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestSystemStateManager	|	 some err in request: $e", ColorsOut.GREEN)
        }

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.GLASS), newState.getCurrentBoxWeight(sys.state.Material.GLASS) )
        assertEquals(prevState.getCurrentBoxWeight(sys.state.Material.PLASTIC), newState.getCurrentBoxWeight(sys.state.Material.PLASTIC))
    }



}