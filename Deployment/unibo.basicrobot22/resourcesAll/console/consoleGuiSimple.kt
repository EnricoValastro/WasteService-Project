package consoles

import connQak.ConnectionType
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import connQak.connQakBase
import consolegui.ButtonAsGui
import it.unibo.kactor.MsgUtil
import consolegui.Utils
 
 
 
class consoleGuiSimple( ) : IObserver {
val stepTime = 350
lateinit var connQakSupport : connQakBase
//val buttonLabels = arrayOf("e","w", "s", "l", "r", "z", "x", "b", "p", "h")
val buttonLabels = arrayOf( "w", "s", "l", "r",  "p", "h")
	
    init{
		createTheGui( connQak.connprotocol )
		Utils.showSystemInfo("after create")
    }

	
	fun createTheGui( connType:ConnectionType  ){
			 var guiName = ""
			 when( connType ){
				 ConnectionType.COAP -> { guiName="GUI-COAP"}
				 ConnectionType.MQTT -> { guiName="GUI-MQTT"}
				 ConnectionType.TCP  -> { guiName="GUI-TCP" }
				 ConnectionType.HTTP -> { guiName="GUI-HTTP"}
			 }
 		val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
 	    concreteButton.addObserver( this )
		connQakSupport = connQakBase.create( connType )
		connQakSupport.createConnection()
	}
	
	override fun update(o: Observable, arg: Any) {
    	  var move = arg as String
		  if( move == "p" ){
			  val msg = MsgUtil.buildRequest("console", "step", "step($stepTime)", connQak.qakdestination )
			  connQakSupport.request( msg )
		  } 
		  else if( move == "e" ){
			  val msg = MsgUtil.buildEvent("console","alarm","alarm(fire)" )
			  connQakSupport.emit( msg )
		  }
 		  else{
			  val msg = MsgUtil.buildDispatch("console", "cmd", "cmd($move)", connQak.qakdestination )
			  connQakSupport.forward( msg )
		  }
	}
}

fun main(){
	println("consoleGuiSimple")
	val appl = consoleGuiSimple(   )
}