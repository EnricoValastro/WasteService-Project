/*
* package unibo.testtransporttrolley

import it.unibo.ctxtransporttrolley.main
import it.unibo.kactor.QakContext
import it.unibo.kactor.QakContext.Companion.getActor
import org.junit.Before
import org.junit.Test
import transporttrolley.state.TransportTrolleyState
import unibo.actor22comm.utils.CommUtils
import unibo.coapobs.TypedCoapTestObserver
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import wasteservice.state.WasteServiceState
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals

class TestTransportTrolleyActor {

    private lateinit var conn: Interaction2021
    private lateinit var obs: TypedCoapTestObserver<TransportTrolleyState>
    private var setupOk = false


    @Before
    fun setup(){
        println("TestTransportTrolleyActor	|	initTest...")
        if(!setupOk){
            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()

            var trolleyActor = getActor("transporttrolley")
            while(trolleyActor == null){
                println("TestTransportTrolleyActor	|	waiting for application start...")
                CommUtils.delay(200)
                trolleyActor = getActor("transporttrolley")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8056, 5)
            } catch (e: Exception) {
                println("TestTransportTrolleyActor	|	connection failed...")
            }
            startObs("localhost:8056")
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
                obs = TypedCoapTestObserver { TransportTrolleyState.fromJsonString(it) }
                var ctx  = "ctxtransporttrolley"
                var act  = "transporttrolley"
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
        println("TestTransportTrolleyActor  |   testPickup...")
        val pickup = "msg(pickingup, dispatch, testunit, transporttrolley, pickingup, 1)"
        conn.forward(pickup)

        assertEquals(obs.getNext().getTTState().toString(), "PICKINGUP")
    }

    @Test
    fun testStateChange2(){
        println("TestTransportTrolleyActor  |   testDropout...")
        val dropout = "msg(droppingout, dispatch, testunit, transporttrolley, droppingout(Plastic), 1)"
        conn.forward(dropout)

        assertEquals(obs.getNext().getTTState().toString(), "DROPPINGOUT")
    }

    @Test
    fun testStateChange3(){
        println("TestTransportTrolleyActor  |   testBackHome...")
        val backHome = "msg(backhome, dispatch, testunit, transporttrolley, backhome, 1)"
        conn.forward(backHome)

        assertEquals(obs.getNext().getTTState().toString(), "IDLE")
    }

}
*
* */