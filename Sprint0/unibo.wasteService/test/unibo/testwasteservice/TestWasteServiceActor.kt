package unibo.testwasteservice

import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext.Companion.getActor
import org.junit.Before
import org.junit.Test
import kotlin.Throws
import java.lang.InterruptedException
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.CommUtils
import wasteservice.state.Material
import wasteservice.state.WasteServiceState
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestWasteServiceActor {

    private lateinit var conn: Interaction2021
    private lateinit var obs: TypedCoapTestObserver<WasteServiceState>
    private var setupOk = false

    private val material = Material.PLASTIC
    private val weight = 200.0

    @Before
    fun before() {
        if(!setupOk) {
            println("TestWasteServiceActor	|	initTest...")
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var wasteserviceactor = getActor("wasteserviceactor")
            while (wasteserviceactor == null) {
                println("TestWasteServiceActor	|	waiting for application start...")
                CommUtils.delay(200)
                wasteserviceactor = getActor("wasteserviceactor")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                println("TestWasteServiceActor	|	connection failed...")
            }
            startObs("localhost:8055")
            obs.getNext()
            setupOk = true
        } else {
            obs.clearHistory()
        }
    }

    fun startObs(addr: String?) {
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    WasteServiceState.fromJsonString(it)
                }
                val ctx = "ctxwasteservice"
                val actor = "wasteserviceactor"
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
    fun testAccept() {
        var asw = ""
        val prevState =obs.currentTypedState!!

        var storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste($material, $weight),1)"
        println("TestWasteServiceActor	|	testaccept on message: $storeWaste")
        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        assertTrue(asw.contains("loadaccept"))

        var newState = obs.getNext()
        assertEquals(prevState.getCurrentBoxWeight(
               material)+weight, newState.getCurrentBoxWeight(material) )

    }

    @Test
    fun testRejected() {
        val prevState =obs.currentTypedState!!
        val storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste(Glass,700),1)"
        println("TestWasteServiceActor	|	testrejected on message: $storeWaste")
        var asw = ""
        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        assertTrue(asw.contains("loadrejected"))
        assertEquals(prevState.getCurrentBoxWeight(material), obs.getNext().getCurrentBoxWeight(material) )

    }


}