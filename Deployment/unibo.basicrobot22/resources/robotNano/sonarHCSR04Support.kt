package robotNano
/*
 -------------------------------------------------------------------------------------------------
 Run SonarAlone and reads data from its output
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil

object sonarHCSR04Support {
	lateinit var reader : BufferedReader
	
	//g++  SonarAlone.c -l wiringPi -o  SonarAlone
	fun create( actor : ActorBasic, todo : String="" ){
		println("sonarHCSR04Support CREATING for ${actor.name}")
		val p = Runtime.getRuntime().exec("sudo ./SonarAlone")
		reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
		startRead( actor )
	}
	


	fun startRead( actor: ActorBasic  ){
		GlobalScope.launch{
			while( true ){
				var data = reader.readLine()
				//println("sonarHCSR04Support data = $data"   )
				if( data != null ){
	 				val m1 = "sonar( $data )"
 					val event = MsgUtil.buildEvent( "sonarsupport","sonarRobot",m1)								
 					//println("sonarHCSR04Support event = $event actor=${actor.name}"   )
					//actor.emit(  event )		//AVOID: better use a stream
					//(streaming)
					actor.emitLocalStreamEvent( event )  
				}
				//delay( 100 )
			}
		}
	}
}