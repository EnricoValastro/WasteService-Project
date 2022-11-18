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
import jssc.*
import jssc.SerialPort.*
import unibo.comm22.serial.SerialConnection

object mbotSupport{

val rotLeftTime : Long   = 610;
val rotRightTime : Long  = 610;
val rotZStepTime  = 58;

	lateinit var owner   : ActorBasic
 	lateinit var conn    : SerialConnection
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
			"msg(w)", "w" -> conn.forward("w")
			"msg(s)", "s" -> conn.forward("s")
			"msg(a)", "a" -> { kotlinx.coroutines.runBlocking{ conn.forward("a") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.forward("h") } }
			"msg(d)", "d" -> { kotlinx.coroutines.runBlocking{ conn.forward("r") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.forward("h") } }
			"msg(l)", "l" -> { kotlinx.coroutines.runBlocking{ conn.forward("l") ;  kotlinx.coroutines.delay(rotLeftTime);   conn.forward("h") } }
			"msg(r)", "r" -> { kotlinx.coroutines.runBlocking{ conn.forward("r") ;  kotlinx.coroutines.delay(rotRightTime);  conn.forward("h") } }
			"msg(z)", "z" -> conn.forward("z")
			"msg(x)", "x" -> conn.forward("x")
			"msg(h)", "h" -> conn.forward("h")
			else -> println("   	%%% mbotSupport | command $cmd unknown")
		}
		
	}
	
	private fun sendToPython( msg : String ){
		println("mbotSupport sendToPython $msg")
		owner.scope.launch{ owner.emit("rotationCmd","rotationCmd($msg)") }
	}
	
 }