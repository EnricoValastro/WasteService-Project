package robotMbot;

import it.unibo.kactor.*
import kotlinx.coroutines.CoroutineScope

class logDevice(name : String, scope: CoroutineScope) : ActorBasic( name, scope ) {
var lastInfo = "start"	
    override suspend fun actorBody(msg: IApplMessage) {
        val info   = msg.msgContent()
        val sender = msg.msgSender()
		if( info != lastInfo ){
			println("   $name |  new data: $info from $sender ")
			lastInfo = info
		}
    }
}