/*
* ---------------------------------------------------------------------------------------------------------------------
* Per eseguire il test è necessario cambiare estensione del file testWasteService.qakx in .qak
* Eliminare tutti i package in "src" e rigenerarli salvando il file testWasteService.qak
* ---------------------------------------------------------------------------------------------------------------------
* */
import it.unibo.ctxwasteservice.main
import it.unibo.kactor.QakContext
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommUtils
import kotlin.test.assertTrue

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
class TestWasteServiceComponent {

    companion object{
        private var setup = false
        private lateinit var conn: Interaction2021
    }

    @Before
    fun setUp(){
        if(!setup) {
            ColorsOut.outappl("TestWasteService     |	setup...", ColorsOut.GREEN)

            object : Thread() {
                override fun run() {
                    main()
                }
            }.start()
            var tTCore = QakContext.getActor("wasteservice")
            while (tTCore == null) {
                ColorsOut.outappl("TestWasteService     |	waiting for application starts...", ColorsOut.GREEN)
                CommUtils.delay(200)
                tTCore = QakContext.getActor("wasteservice")
            }
            try {
                conn = TcpClientSupport.connect("localhost", 8055, 5)
            } catch (e: Exception) {
                ColorsOut.outappl("TestWasteService 	|	TCP connection failed...", ColorsOut.GREEN)
            }
            setup = true
        }
    }

    @Test
    fun testLoadAccept(){
        ColorsOut.outappl("TestWasteService   |   testLoadAccept", ColorsOut.GREEN)

        var answ = ""
        var storewaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 300),1)"
        try {
            answ = conn.request(storewaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteService   |   request failed...", ColorsOut.GREEN)
        }
        assertTrue(answ.contains("loadaccept"))
    }
    @Test
    fun testLoadReject(){
        ColorsOut.outappl("TestWasteService   |   testLoadRejected", ColorsOut.GREEN)

        var answ = ""
        var storewaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 600),1)"
        try {
            answ = conn.request(storewaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteService   |   request failed...", ColorsOut.GREEN)
        }
        assertTrue (answ.contains("loadrejected"))
    }

    @Test
    fun testMultipleRequest(){
        ColorsOut.outappl("TestWasteService   |   testMultipleReqquest", ColorsOut.GREEN)

        var answ = ""
        var storewaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 100),1)"
        try {
            answ = conn.request(storewaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteService   |   request failed...", ColorsOut.GREEN)
        }
        assertTrue (answ.contains("loadaccept"))

        storewaste = "msg(storewaste, request, testunit, wasteservice, storewaste(PLASTIC, 300),1)"
        try {
            answ = conn.request(storewaste)
        } catch (e: Exception) {
            ColorsOut.outappl("TestWasteService   |   request failed...", ColorsOut.GREEN)
        }
        assertTrue ( answ.contains("loadrejected") )
    }


}