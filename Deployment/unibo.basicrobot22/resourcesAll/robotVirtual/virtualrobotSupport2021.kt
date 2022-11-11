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
import unibo.comm22.wshttp.WsHttpConnection
import unibo.comm22.interfaces.Interaction2021
import unibo.robot.MsgRobotUtil
import unibo.comm22.ws.WsConnection
 
//import it.unibo.interaction.MsgRobotUtil

 //A support for using the virtual robot
 


object virtualrobotSupport2021 {
	private var port     = 0
	lateinit var owner      : ActorBasic
	lateinit var robotsonar	: ActorBasic
	private lateinit var hostName : String 	
	private lateinit var support21   : Interaction2021 	 
	private lateinit var support21ws : WsConnection //extends Interaction2021
    private val forwardlongtimeMsg  = "{\"robotmove\":\"moveForward\", \"time\": 1000}"
    private val backwardlongtimeMsg = "{\"robotmove\":\"moveBackward\", \"time\": 1000}"

	var traceOn = false
	
	init{
		println("virtualrobotSupport2021 | init ... ")
		//WsHttpConnection.trace = false
	}
/*
val doafterConn : (CoroutineScope, WsHttpConnection) -> Unit =
     fun(scope, support ) {
		val obs  = WsSupportObserver( owner.getName() )
        println("virtualrobotSupport2021 | doafterConn REGISTER an observer for the WsHttpConnection")
		support.addObserver( obs )
}
*/


	fun create( owner: ActorBasic, hostNameStr: String, portStr: String, trace : Boolean = false  ){
 		this.owner   = owner	 
 		this.traceOn = trace
 		//initClientConn
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
				//WsHttpConnection.trace = true
            	support21    = WsHttpConnection.createForHttp( "$hostNameStr:$portStr" ) ///api/move built-in
				support21ws  = WsConnection.create( "$hostNameStr:8091" )    
            	//println("		--- virtualrobotSupport2021 |  created (ws) $hostNameStr:$portStr $support21 $support21ws")	
				//support21ws.wsconnect( doafterConn )  //2021
//2022 Il POJO it.unibo.qak21.basicrobot informa basicrobot di una collisione				 
				val obs  = WsSupportObserver( owner.getName() )
				(support21ws as WsConnection).addObserver(obs) 			  
//				//ACTIVATE the robotsonar as the beginning of a pipe
				robotsonar = virtualrobotSonarSupportActor("robotsonar", null)
				owner.context!!.addInternalActor(robotsonar)  
			  	println("		--- virtualrobotSupport2021 | has created the robotsonar")	
			 }catch( e:Exception ){
                 println("		--- virtualrobotSupport2021 | ERROR $e")
             }	
	}
	
	fun trace( msg: String ){
		if( traceOn )  println("		--- virtualrobotSupport2021 trace | $msg")
	}

    fun move(cmd: String) {	//cmd is written in application-language
		//println("		--- virtualrobotSupport2021 |  moveeeeeeeeeeeeeeeeeeeeee $cmd ")
		val msg = translate( cmd )
		trace("move  $msg")
		if( cmd == "w" || cmd == "s"){  //doing aysnch
			//println("		--- virtualrobotSupport2021 |  wwwwwwwwwwwwwwwwwwwwwwwwww $support21ws")
			support21ws.forward(msg)	//aysnch => no immediate answer 
			return
		}
		//Comunicazione sincrona con il VirtualRobot via HTTP
		val answer = support21.forward(msg) //,"$hostName:$port/api/move"
		trace("		--- virtualrobotSupport2021 | answer=$answer")
		//REMEMBER: answer={"endmove":"true","move":"alarm"} alarm means halt
		val ajson = JSONObject(answer)
		if( ajson.has("endmove") && ajson.get("endmove")=="false"){
			owner.scope.launch{  owner.emit("obstacle","obstacle(virtual)") }
		}
    } 
    //translates application-language in cril
    fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = forwardlongtimeMsg  		
				"msg(s)", "s" -> jsonMsg = backwardlongtimeMsg  
				"msg(a)", "a" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(d)", "d" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				"msg(l)", "l" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(r)", "r" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				//"msg(z)", "z" -> not implemented
				//"msg(x)", "x" -> not implemented
				"msg(h)", "h" -> jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm','arg': 100 }"
				else -> println("		--- virtualrobotSupport2021 | command $cmd unknown")
			}
 			return jsonMsg
		}
	fun terminate(){
		robotsonar.terminate()
	}	
	
}//virtualrobotSupport2021

 







