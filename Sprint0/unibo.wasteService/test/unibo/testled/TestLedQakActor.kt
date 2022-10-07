/*
* package unibo.testled

import it.unibo.ctxledqak.main
import it.unibo.kactor.QakContext
import led.state.LedState
import org.junit.Before
import org.junit.Test

import unibo.actor22comm.utils.CommUtils
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals

class TestLedQakActor {

    private lateinit var conn: Interaction2021
    private lateinit var obs: TypedCoapTestObserver<LedState>
    private var setupOk = false


    @Before
    fun setup(){
        println("TestLedQakActor	|	initTest...")
        if(!setupOk){
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var ledActor = QakContext.getActor("ledqakactor")
            while(ledActor == null){
                println("TestLedQakActor	|	waiting for application start...")
                CommUtils.delay(200)
                ledActor = QakContext.getActor("ledqakactor")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8057, 5)
            } catch (e: Exception) {
                println("TestLedQakActor	|	connection failed...")
            }
            startObs("localhost:8057")
            obs.getNext()
            setupOk = true
        }
        else{
            obs.clearHistory()
        }
    }

    fun startObs(addr : String){
        val setupOk = ArrayBlockingQueue<Boolean>(1)
        object : Thread(){
            override fun run(){
                obs = TypedCoapTestObserver { LedState.fromJsonString(it) }
                var ctx  = "ctxledqak"
                var act  = "ledqakactor"
                var path = "$ctx/$act"
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
    fun testStateChange1(){
        println("TestLedQakActor  |   testOn...")
        val turnOn = "msg(turnOn, dispatch, testunit, ledqakactor, turnOn, 1)"
        conn.forward(turnOn)

        assertEquals(obs.getNext().getTTState().toString(), "ON")
    }

    @Test
    fun testStateChange2(){
        println("TestLedQakActor  |   testOff...")
        val turnOff = "msg(turnOff, dispatch, testunit, ledqakactor, turnOff, 1)"
        conn.forward(turnOff)

        assertEquals(obs.getNext().getTTState().toString(), "OFF")
    }

    @Test
    fun testStateChange3(){
        println("TestLedQakActor  |   testBlink...")
        val blink = "msg(blink, dispatch, testunit, ledqakactor, blink, 1)"
        conn.forward(blink)

        assertEquals(obs.getNext().getTTState().toString(), "BLINKING")
    }
}*/