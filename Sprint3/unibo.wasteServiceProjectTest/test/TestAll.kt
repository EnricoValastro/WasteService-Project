import it.unibo.ctxwasteservice.main
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import it.unibo.radarSystem22.domain.utils.BasicUtils
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import sys.state.Material
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
class TestAll {
    companion object {
        private var setup = false
        private lateinit var conn: Interaction2021
        private lateinit var obs: TypedCoapTestObserver<SystemState>
    }

    @Before
    fun setUp(){
        if(!setup) {
            ColorsOut.outappl("TestAllSystem	|	setup...", ColorsOut.GREEN)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ws = QakContext.getActor("wasteservice")
            while (ws == null) {
                ColorsOut.outappl("TestAllSystem	|	waiting for application starts...", ColorsOut.GREEN)
                CommUtils.delay(200)
                ws = QakContext.getActor("wasteservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestAllSystem	|	TCP connection failed...", ColorsOut.GREEN)
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
    fun testSystem1(){
        ColorsOut.outappl("TestAllSystem	|	testing system without stop", ColorsOut.GREEN)
        var storewaste = MsgUtil.buildRequest("testunit", "storewaste", "storewaste(GLASS, 100)", "wasteservice")
        lateinit var state : SystemState
        var ans = ""
        //var produce = MsgUtil.buildRequest("testunit", "produce", "produce(30)", "sonarqak22varesi")
        //var produce2 = MsgUtil.buildRequest("testunit", "produce", "produce(50)", "sonarqak22varesi")
        ColorsOut.outappl("TestAllSystem	|	sending request: ${storewaste.toString()}", ColorsOut.GREEN)
        ans = conn.request(storewaste.toString())

        assertTrue(ans.contains("loadaccept"))

        state = obs.getNext()
        assertEquals(100.0, state.getCurrentBoxWeight(Material.GLASS))
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("HOME", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())

        state = obs.getNext()
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("PICKINGUP", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("HOME", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

    }
    @Test
    fun testSystem2(){
        ColorsOut.outappl("TestAllSystem	|	testing system with stop&resume", ColorsOut.GREEN)
        lateinit var state : SystemState
        var ans = ""
        var storewaste = MsgUtil.buildRequest("testunit", "storewaste", "storewaste(GLASS, 200)", "wasteservice")
        var produce = MsgUtil.buildRequest("testunit", "produce", "produce(20)", "sonarqak22varesi")
        var produce2 = MsgUtil.buildRequest("testunit", "produce", "produce(50)", "sonarqak22varesi")
        ColorsOut.outappl("TestAllSystem	|	sending request: ${storewaste.toString()}", ColorsOut.GREEN)
        conn.request(storewaste.toString())
        conn.request(produce.toString())

        state = obs.getNext()
        assertEquals(300.0, state.getCurrentBoxWeight(Material.GLASS))
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("HOME", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())

        state = obs.getNext()
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("PICKINGUP", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("STOPPED", state.getCurrState().toString())

        conn.request(produce2.toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("STOPPED", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("DROPPINGOUT", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("GLASSBOX", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("HOME", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())


    }

    @Test
    fun testSystem3(){
        ColorsOut.outappl("TestAllSystem	|	testing system with stop", ColorsOut.GREEN)
        var storewaste = MsgUtil.buildRequest("testunit", "storewaste", "storewaste(PLASTIC, 200)", "wasteservice")
        var produce = MsgUtil.buildRequest("testunit", "produce", "produce(30)", "sonarqak22varesi")
        lateinit var state : SystemState
        var ans = ""

        ColorsOut.outappl("TestAllSystem	|	sending request: ${storewaste.toString()}", ColorsOut.GREEN)
        ans = conn.request(storewaste.toString())
        conn.request(produce.toString())

        assertTrue(ans.contains("loadaccept"))

        state = obs.getNext()
        assertEquals(200.0, state.getCurrentBoxWeight(Material.PLASTIC))
        assertEquals("OFF", state.getCurrLedState().toString())
        assertEquals("HOME", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())

        state = obs.getNext()
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("PICKINGUP", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("INDOOR", state.getCurrPosition().toString())
        assertEquals("IDLE", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("BLINKING", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("MOVING", state.getCurrState().toString())

        state = obs.getNext()
        assertEquals("ON", state.getCurrLedState().toString())
        assertEquals("ONTHEROAD", state.getCurrPosition().toString())
        assertEquals("STOPPED", state.getCurrState().toString())

    }






}