package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the support for the mbot
 "/dev/ttyUSB0"
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object mbotSupport{

val rotLeftTime : Long   = 610;
val rotRightTime : Long  = 610;
val rotZStepTime  = 58;

	lateinit var owner   : ActorBasic
 	lateinit var conn    : SerialPortConnSupport
	var dataSonar        : Int = 0 ; //Double = 0.0
 			
	fun create( owner: ActorBasic, port : String ="/dev/ttyUSB0"  )  { //See robotSupport
		this.owner = owner	//
		initConn( port   )
	}
	
	private fun initConn( port : String ){ 
		try {
			//println("   	%%% mbotSupport | initConn starts port=$port")
			val serialConn = JSSCSerialComm()
			conn = serialConn.connect(port)	//returns a SerialPortConnSupport
			println("    	%%% mbotSupport |  initConn port=$port conn= $conn")						
 			val realsonar = robotDataSourceArduino("realsonar", owner,   conn )
			//Context injection
				owner.context!!.addInternalActor(realsonar)
			//ACTIVATE the sonar on MBot
			    runBlocking{realsonar.autoMsg("start", "start(ok)")}
		  		println("   	%%% mbotSupport | has created the realsonar owner=${owner.name}")
		}catch(  e : Exception) {
			println("   	%%% mbotSupport |  ERROR ${e }"   );
		}		
	}
	
	/*
 	 Aug 2019
     The moves l, r, z, x can be executed  
 	  by the Python application robotCmdExec that exploits GY521
    */
	fun  move( cmd : String ){
		//println("  	%%% mbotSupport | move cmd=$cmd ")
		when( cmd ){
			"msg(w)", "w" -> conn.sendALine("w")
			"msg(s)", "s" -> conn.sendALine("s")
			"msg(a)", "a" -> { kotlinx.coroutines.runBlocking{ conn.sendALine("a") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.sendALine("h") } }
			"msg(d)", "d" -> { kotlinx.coroutines.runBlocking{ conn.sendALine("r") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.sendALine("h") } }
			"msg(l)", "l" -> { kotlinx.coroutines.runBlocking{ conn.sendALine("l") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.sendALine("h") } }
			"msg(r)", "r" -> { kotlinx.coroutines.runBlocking{ conn.sendALine("r") ;  kotlinx.coroutines.delay(rotRightTime);  conn.sendALine("h") } } 
			"msg(z)", "z" -> conn.sendALine("z")  
			"msg(x)", "x" -> conn.sendALine("x")  
			"msg(h)", "h" -> conn.sendALine("h")
			else -> println("   	%%% mbotSupport | command $cmd unknown")
		}
		
	}
	
	private fun sendToPython( msg : String ){
		println("mbotSupport sendToPython $msg")
		owner.scope.launch{ owner.emit("rotationCmd","rotationCmd($msg)") }
	}
	
 }