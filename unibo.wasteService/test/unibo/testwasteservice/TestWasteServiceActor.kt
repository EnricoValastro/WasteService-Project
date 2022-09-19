package unibo.testwasteservice

import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext.Companion.getActor
import kotlin.Throws
import java.lang.InterruptedException
import unibo.testwasteservice.TestWasteServiceActor
import org.json.JSONObject
import unibo.testwasteservice.CoapTestObserver
import it.unibo.kactor.ActorBasic
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import unibo.actor22comm.utils.CommUtils
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import java.lang.Exception
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ArrayBlockingQueue

class TestWasteServiceActor {
    private val material = "Plastic"
    private val weight = 200.0
    @Test
    @Throws(InterruptedException::class)
    fun testAccept() {
        obs!!.clearQueue()
        var asw = ""
        val prevState = fromJsonString(obs!!.currState) //StateSerializer.INSTANCE.fromJsonString(obs.getCurrState()).g;
        val storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste($material,$weight),1)"
        println("TestWasteServiceActor	|	testaccept on message: $storeWaste")
        try {
            asw = conn!!.request(storeWaste)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Assert.assertTrue(asw.contains("loadaccept"))

        //double state = obs.getCurrState()
        //assertEquals(prevState+material, state, 0);
    }

    @Test
    fun testRejected() {
        val storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste(Glass,700),1)"
        println("TestWasteServiceActor	|	testrejected on message: $storeWaste")
        var asw = ""
        try {
            asw = conn!!.request(storeWaste)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Assert.assertTrue(asw.contains("loadrejected"))
    }

    private fun fromJsonString(obsString: String): Double {
        val jsonObject = JSONObject(obsString)
        return jsonObject.getDouble(material)
    }

    companion object {
        private var conn: Interaction2021? = null
        private var obs: CoapTestObserver? = null
        @BeforeClass
        @Throws(InterruptedException::class)
        fun initTest() {
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
            obs!!.takeNext()
        }

        @Throws(InterruptedException::class)
        fun startObs(addr: String?) {
            val setupOk: BlockingQueue<Boolean?> = ArrayBlockingQueue<Any?>(1)
            object : Thread() {
                override fun run() {
                    obs = CoapTestObserver()
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
    }
}