package connQak 
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.IApplMessage
 
enum class ConnectionType {
    TCP, MQTT, COAP, HTTP
}

abstract class connQakBase() {
lateinit var currQakConn  : connQakBase
	
	companion object{
	fun create(connType: ConnectionType) : connQakBase{
		  showSystemInfo()
		  when( connType ){
				 ConnectionType.MQTT ->  {return connQakMqtt( )}  
				 ConnectionType.TCP ->   {return connQakTcp( ) }  
				 ConnectionType.COAP ->  {return connQakCoap( )}  
  				 ConnectionType.HTTP ->  {return connQakHttp( )} 
//   				 else -> //println("WARNING: protocol unknown")
 		  }		
	}
	fun showSystemInfo(){
		println(
			"connQakBase  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		println(
			"connQakBase  | NUM of threads="+ Thread.activeCount() +
					" currentThread=" + Thread.currentThread() );
	}
	}//object

	
	  abstract fun createConnection( )     
      abstract fun forward( msg : IApplMessage )
      abstract fun request( msg : IApplMessage )
      abstract fun emit( msg : IApplMessage )
	
}

 
 
 