import it.unibo.ctxwasteservice.main
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import org.junit.Before
import org.junit.Test
import sys.state.SystemState
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommUtils
import java.util.concurrent.ArrayBlockingQueue

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
                conn = TcpClientSupport.connect("localhost", 8055)
            } catch (e: Exception) {
                ColorsOut.outappl("TestSystem	|	TCP connection failed...", ColorsOut.GREEN)
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
    fun test1(){
        var ws = QakContext.getActor("wasteservice")
        MsgUtil.
    }




}