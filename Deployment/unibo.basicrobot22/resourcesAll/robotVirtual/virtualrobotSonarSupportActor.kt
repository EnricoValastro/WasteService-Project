package robotVirtual

import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import java.io.BufferedReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.IApplMessage
 
 


class virtualrobotSonarSupportActor( name : String, val clientSocket : Socket? ) : ActorBasic( name ) {
private var sensorObserver : Job? = null
companion object {
	val eventId = "sonarRobot"
}
		init{
		println("$tt $name | CREATING")		
	}



    override suspend fun actorBody(msg : IApplMessage){
// 		println("$tt $name | received  $msg "  )  //perceives all the application events
		if( msg.msgId() == "sonarstart"){
			println("$tt $name | STARTING")
			startSensorObserver()
		}
	}
	


   fun startSensorObserver(  ) {
	if( clientSocket != null ){
		val inFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
		val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
	    sensorObserver = scope.launch {
 				println("sensorObserver STARTS ")
                while (true) {
                    try {
                        val inpuStr = inFromServer.readLine()
						println("sensorObserver inpuStr= $inpuStr")
						if( inpuStr == null ) break;		//JUNE2020
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //println("inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //println( "type: " + jsonObject.getString("type"))
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> println("webpage-ready ")
                            "sonar-activated" -> {
                                val jsonArg   = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance  = jsonArg.getInt("distance")							 
								val m1 = "sonar( $distance, $sonarName )"
								println( "sensorObserver emit local_sonar"   )
								emit("local_sonar",m1)
                            }
                            "collision" -> {
 								val m1 = "sonar( 5 )"
                                val event = MsgUtil.buildEvent( name,"sonar",m1)
                                emitLocalStreamEvent( event )		//not propagated to remote actors
                              }
                        }
                    } catch (e: Exception) {
                        //e.printStackTrace()
						println("startSensorObserver ERROR: ${e.message}")
						break
                    }
                }
 				println("sensorObserver ENDS, since has received null ")
            }
	 }
    }//startSensorObserver 

@Override
fun terminate(){
	terminatevr()
} 
fun terminatevr(){
	if(sensorObserver != null ) sensorObserver!!.cancel()
	println("TERMINATES sensorObserver")
}
}

 