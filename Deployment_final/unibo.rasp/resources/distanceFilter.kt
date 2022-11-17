
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import it.unibo.kactor.IApplMessage


class distanceFilter (name : String ) : ActorBasic( name ) {
val LimitDistance = 10

    override suspend fun actorBody(msg: IApplMessage) {
		if( msg.msgSender() == name) return //AVOID to handle the event emitted by itself
  		elabData( msg )
 	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
	  suspend fun elabData( msg: IApplMessage ){ //OPTIMISTIC
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
//  		println("$tt $name |  data = $data ")
		val Distance = Integer.parseInt( data ) 
/*
 * Emit a sonarRobot event to test the behavior with MQTT
 * We should avoid this pattern
*/	
//	 	val m0 = MsgUtil.buildEvent(name, "sonarRobot", "sonar($data)")
//	 	emit( m0 )
 		if( Distance < LimitDistance ){
	 		val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($data)")
			//println("$tt $name |  emit m1= $m1")
			emitLocalStreamEvent( m1 ) //propagate event obstacle
     	}else{
			//println("$tt $name | DISCARDS $Distance ")
 		}				
 	}
}