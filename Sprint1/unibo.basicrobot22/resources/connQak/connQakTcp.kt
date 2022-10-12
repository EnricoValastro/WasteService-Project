package connQak

import it.unibo.kactor.IApplMessage
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.interfaces.Interaction2021

class connQakTcp(  ) : connQakBase( ){
	lateinit var conn   : Interaction2021 //IConnInteraction
	
	override fun createConnection( ){ //hostIP: String, port: String
		conn = TcpClientSupport.connect( hostAddr, port.toInt(),10 )
		println("connQakTcp createConnection $hostAddr:$port")
	}
	
	override fun forward( msg: IApplMessage ){
		println("connQakTcp | forward: $msg")	
 		conn.forward( msg.toString()  )
	}
	
	override fun request( msg: IApplMessage ){
 		conn.forward( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveMsg()
		println("connQakTcp | answer= $answer")		
	}
	
	override fun emit( msg: IApplMessage ){
 		conn.forward( msg.toString()  )
	}	
}