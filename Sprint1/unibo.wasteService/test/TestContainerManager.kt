import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.CommUtils
import wasteservice.state.Material
import wasteservice.state.ServiceAreaState
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestContainerManager {

    private lateinit var conn: Interaction2021
    private lateinit var obs: TypedCoapTestObserver<ServiceAreaState>
    private var setup = false

    @Before
    fun setUp(){
        if(!setup) {
            println("TestContainerManager	|	setup...")

            startMockCtx()

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var wSHandler = QakContext.getActor("wasteservicehandler")
            while (wSHandler == null) {
                println("TestContainerManager	|	waiting for application starts...")
                CommUtils.delay(200)
                wSHandler = QakContext.getActor("wasteservicehandler")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                println("TestContainerManager	|	TCP connection failed...")
            }
            startObs("localhost:8055")
            obs.getNext()
            setup = true
        } else {
            obs.clearHistory()
        }
    }

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

    fun startObs(addr: String?) {
        val setupOk = ArrayBlockingQueue<Boolean>(1)

        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    ServiceAreaState.fromJsonString(it)
                }
                val ctx = "ctxwasteservice"
                val actor = "containermanager"
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
        println("TestContainerManager	|	testing update...")

        var asw = ""
        val prevState =obs.currentTypedState!!

        var storeWaste = "msg(storewaste, request, testunit, wasteservicehandler, storewaste(PLASTIC, 200),1)"

        try {
            asw = conn.request(storeWaste)
        } catch (e: Exception) {
            println("TestContainerManager	|	 some err in request: $e")
        }
        assertTrue(asw.contains("loadaccept"))

        var newState = obs.getNext()
        println(newState.getCurrentBoxWeight(Material.PLASTIC))
        assertEquals(prevState.getCurrentBoxWeight(Material.PLASTIC)+200.0, newState.getCurrentBoxWeight(Material.PLASTIC) )
        assertEquals(prevState.getCurrentBoxWeight(Material.GLASS), newState.getCurrentBoxWeight(Material.GLASS))

    }



}