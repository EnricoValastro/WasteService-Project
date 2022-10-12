package robotVirtual

import org.json.JSONObject
import it.unibo.kactor.*
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import unibo.comm22.utils.ColorsOut
import unibo.comm22.ws.WsConnSysObserver
import unibo.comm22.utils.CommUtils


/*
  Oggetto che informa l'owner in caso di collisione
*/ 
class WsSupportObserver( val owner:String, val vrsupport : virtualrobotSupport2021) : WsConnSysObserver( owner) {
 var stepok = MsgUtil.buildDispatch("wsobs","stepok","stepok(done)",owner )
 var stepko = MsgUtil.buildDispatch("wsobs","stepko","stepko(todo)",owner )


	fun backALittle(){
		runBlocking {
			ColorsOut.outappl("WsSupportObserver backALittleeeeeeeeeeeee", ColorsOut.BLUE);
			vrsupport.move("s")
			kotlinx.coroutines.delay(50L)
			vrsupport.move("h")
		}
	}
	override fun update( data : String ) {
 		ColorsOut.out("WsSupportObserver update $data owner=$owner   ", ColorsOut.MAGENTA);
        val msgJson = JSONObject(data)
        //println("       &&& WsSupportObserver  | update msgJson=$msgJson" ) //${ aboutThreads()}
		val ownerActor = sysUtil.getActor(owner)
		if( ownerActor == null ) {
			val ev = CommUtils.buildEvent( "wsconn", "wsEvent", data  );
			ColorsOut.out("       &&& WsSupportObserver  | ownerActor null ev=$ev", ColorsOut.MAGENTA )
			return
		}
		var target : String

		if( msgJson.has("collision") ){
			var move = msgJson.getString("collision")
			ColorsOut.outappl("WsSupportObserver move=$move}", ColorsOut.GREEN);
			if( move == "moveForward") backALittle()
 		}
		if( msgJson.has("target")   ){
			target = msgJson.getString("target")
			runBlocking {
				ColorsOut.out("WsSupportObserver emits:obstacle($target)}", ColorsOut.GREEN);
				ownerActor!!.emit("obstacle","obstacle($target)")
			}
		}




	}
	

}