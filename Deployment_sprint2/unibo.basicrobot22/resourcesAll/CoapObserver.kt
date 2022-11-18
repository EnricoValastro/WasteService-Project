import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 
 
object  CoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8020"		//5683 default
	private val context     = "ctxbasicrobot"
 	private val destactor   = "basicrobot"
 



	 fun activate(  ){ 
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   println("CoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("CoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
            } 
            override fun onError() {
                println("CoapObserver | FAILED")
            }
        })		
	}
 }

 


fun main( ) {
		CoapObserver.activate()
		System.`in`.read()   //to avoid exit
 }