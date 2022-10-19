import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext.Companion.getActor
import kotlinx.coroutines.*
import org.junit.Before
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

    private lateinit var conn: Interaction2021
    private lateinit var obs: TypedCoapTestObserver<ServiceAreaState>   //Un observer di tipo ServiceAreaState
    private var setup = false

    @Before
    fun testSetup(){
        if(!setup){
            println("TestWasteServiceHandler    |   setup...")

            startMockCtx()

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
            startObs("localhost:8055")
            obs.getNext()
            setup = true
        }else{
            obs.clearHistory()
        }
    }

    fun startMockCtx(){
        println("TestWasteServiceHandler    |   launching mockCtx...")
        val mockCtxScope = CoroutineScope(CoroutineName("CtxScope"))
        //while(true){
        mockCtxScope.launch {
            val selectorManager = SelectorManager(Dispatchers.IO)
            val serverSocket    = aSocket(selectorManager).tcp().bind("127.0.0.1", 8056)
            val socket = serverSocket.accept()
            println("Accepted $socket")
            val receiveChannel = socket.openReadChannel()
            try {
                val name = receiveChannel.readUTF8Line()
            } catch (e: Throwable) {
                socket.close()
            }
        }
        //}

    }

    fun startObs(addr: String?) {
        println("TestWasteServiceHandler    |   starting observer...")
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread() {
            override fun run() {
                obs = TypedCoapTestObserver{
                    wasteservice.state.ServiceAreaState.fromJsonString(it)
                }
                val ctx = "ctxwasteservice"
                val act = "wasteservicehandler"
                val path = "$ctx/$act"
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





}