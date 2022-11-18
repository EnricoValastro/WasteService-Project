package unibo.robot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the proper support for each specific robot type
 
 NOV 2019:
 The operation create accept as last argument (filter) an ActorBasic to be used
 as a data-stream handler
 
 The operation subscribeToFilter subscribes to the given data-stream handler
 (dsh) another ActorBasic that should work as a data-stream handler
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
import org.json.JSONObject
import java.io.File
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking
import robotMbot.mbotSupport.conn

//import robotMbot.robotDataSourceArduino
 


object robotSupport{
	lateinit var robotKind  :  String
	var endPipehandler      :  ActorBasic? = null 
	
	fun readStepTime(   ) : String{  
 		val config = File("stepTimeConfig.json").readText(Charsets.UTF_8)
		val jsonObject   = JSONObject( config )
		return jsonObject.getString("step") 
	}
	
	fun create( owner: ActorBasic, configFileName: String, endPipe: ActorBasic? = null ){
		endPipehandler      =  endPipe
 		val config = File("${configFileName}").readText(Charsets.UTF_8)
		val jsonObject   = JSONObject( config )
		val hostAddr     = jsonObject.getString("ipvirtualrobot") 
		robotKind        = jsonObject.getString("type") 
		val robotPort    = jsonObject.getString("port") 
		println( "		--- robotSupport | CREATING for $robotKind host=$hostAddr port=$robotPort owner=$owner" )

		when( robotKind ){		
			//"mockrobot"  ->  { robotMock.mockrobotSupport.create(  ) }
			"virtual"    ->  { robotVirtual.virtualrobotSupport2021.create( owner, hostAddr, robotPort) }
  			"realnano"   ->  {
				robotNano.nanoSupport.create( owner )
 				val realsonar = robotNano.sonarHCSR04SupportActor("realsonar")
				//Context injection  
				owner.context!!.addInternalActor(realsonar)  
  				println("		--- realnano robotSupport | has created the realsonar")
			}
			"realmbot" -> {
				robotMbot.mbotSupport.create(owner, robotPort)
				/*
				val realsonar = robotMbot.robotDataSourceArduino("realsonar", owner, conn)
				//Context injection
				owner.context!!.addInternalActor(realsonar)  
  				println("		--- realmbot robotSupport | has created the realsonar")*/
			}
 			else -> println( "		--- robotSupport | robot $robotKind unknown" )
 		}
	}
	
	fun subscribe( obj : ActorBasic ) : ActorBasic {
		if( endPipehandler != null ) endPipehandler!!.subscribe( obj )
		return obj
	}
	 


	fun move( cmd : String ){ //cmd = w | a | s | d | h
 		//println("robotSupport move cmd=$cmd robotKind=$robotKind" ) 
		when( robotKind ){
			//"mockrobot"  -> { robotMock.mockrobotSupport.move( cmd ) 					  }
			"virtual"    -> { robotVirtual.virtualrobotSupport2021.move(  cmd ) 	  }
  			"realnano"   -> { robotNano.nanoSupport.move( cmd)	}
            "realmbot"   -> { robotMbot.mbotSupport.move( cmd )	}
			else         -> println( "		--- robotSupport: move| robot unknown")
		}		
	}
	
	fun terminate(){
		when( robotKind ){
			"mockrobot"  -> {  					                  }
			"virtual"    -> { robotVirtual.virtualrobotSupport2021.terminate(  ) 	  }
 			"realmbot"   -> { /* robotMbot.mbotSupport.terminate(  ) */	}
 			"realnano"   -> { robotNano.nanoSupport.terminate( )	}
			else         -> println( "		--- robotSupport | robot unknown")
		}		
		
	}
	
	fun createSonarPipe(robotsonar: ActorBasic?){
 		if( robotsonar != null ){ 
			//runBlocking{
				//ACTIVATE THE DATA SOURCE  
				//MsgUtil.sendMsg("robotSupport", "sonarstart", "sonarstart(do)", robotsonar)
		 		//SET THE PIPE  
		 		robotsonar.
		 			subscribeLocalActor("datacleaner").
		 			subscribeLocalActor("distancefilter").
		 			subscribeLocalActor("basicrobot")		//in order to perceive obstacle
			//}
			println("robotSupport | SONAR PIPE DONE NO runBlocking")
		}else{
	 		println("robotSupport | WARNING: sonar NOT FOUND")
	 	}		
	}
}