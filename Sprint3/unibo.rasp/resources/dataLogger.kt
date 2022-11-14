
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Struct
import alice.tuprolog.Term
import java.io.PrintWriter
import java.io.FileWriter
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.IApplMessage


class dataLogger(name : String) : ActorBasic(name){
	var pw : PrintWriter
	
 	init{
		pw = PrintWriter( FileWriter(name+".txt") )
 	}

	override suspend fun actorBody(msg: IApplMessage) {
  		elabData( msg )
		emitLocalStreamEvent(msg)	//propagate ... 
	}
 
 	protected suspend fun elabData( msg: IApplMessage ){
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
		println("	-------------------------------------------- $name data=$data")
   		pw.append( "$data\n " )
		pw.flush()
     }

}