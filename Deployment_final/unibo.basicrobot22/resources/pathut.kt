 /*
 pathexecutil.kt
 ---------------------------------------------------------------
 We use both the actors defined in 2021
 (e.g. it.unibo.robotService.BasicStepRobotActor from project it.unibo.kotlinSupports)
 and the actors defined in 2020
 (e.g. it.unibo.kactor.ActorBasicFsm from project it.unibo.qakactor)
 */

import org.json.JSONObject
import it.unibo.kactor.*
import java.util.Scanner
import alice.tuprolog.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
 

object pathut{
	var pathDone = ""
	var curMove  = "unknown"	
	var curPath  = ""
	var wenvAddr = "localhost"
	lateinit var  master: ActorBasicFsm
	lateinit var  owner:  String


	fun setPath(path: String)  {
		println("setPath:$path")
		//path : "wwlw"
		//Potrei ricevere dal pianificatore la stringa: "[w, w, l, w]"
		//Meglio ricevere wwlw
		curPath = path
			.replace(" ","")
			.replace(",","")
			.replace("[","")
			.replace("]","")
	}

	fun setPathFromRequest(msg: IApplMessage)  {
		println("setPathFromRequest $msg")
		var payload     = msg.msgContent().replace("'","")
		//payload : {"path":"wwlw" , "owner":"pathexecCaller"}
		println("setPathFromRequest $payload")
		curPath = JSONObject(payload).getString("path")
	}
	
	fun getPathTodo() : String{
		if( curPath.length == 0 ) return "none"
		else return curPath
	}

	suspend fun doNextMove( master: ActorBasicFsm) {
		val move = nextMove()
		//println("pathexecutil | doNextMove=$move")
		delay(150)  	//to reduce path speed
		if( move.length == 0 ) {
			//master.autoMsg("pathdone","pathtodo($curPath)")
			//println("!!!!!!!!!!! SEND pathdone to OWNER=$owner" )
		}else{
			doMove(master, move)
		}
	}
		
	fun nextMove() : String{
		//println("pathexecutil | nextMove curPath=$curPath")
		if( curPath.length == 0 ) return ""
		//curPath still has moves
		val move = ""+curPath[0]
		curPath  = curPath.substring(1)
		return move
	}



	suspend fun doMove(master: ActorBasicFsm, moveTodo: String ){
		println("pathexecutil | doMove moveTodo=$moveTodo")
/*		
 //robot.send(ApplMsgs.stepRobot_step("appl", "350"))
 //support.//
		//val MoveAnsw = CallRestWithApacheHTTP.doMove(moveTodo)
		curMove = moveTodo
		when( curMove ){
			"p" -> robot.send(ApplMsgs.stepRobot_step("appl", "350"))
			"l" -> robot.send(ApplMsgs.stepRobot_l("appl"))
			"r" -> robot.send(ApplMsgs.stepRobot_r("appl"))
			else -> println("$curMove uknown")
		}
 */
		
		/*
		println("pathexecutil | doMove $moveTodo MoveAnsw=$MoveAnsw")
 
		val answJson = JSONObject( MoveAnsw ) 
		//println("pathexecutil | doMove $moveTodo answJson=$answJson")
		if( ( answJson.has("endmove") && answJson.getString("endmove") == "true")
			|| answJson.has("stepDone") ){
			pathDone = pathDone+moveTodo
			master.autoMsg("moveok","move($moveTodo)")
		}else{
			master.autoMsg("pathfail","pathdone($pathDone)")
			//println("!!!!!!!!!!!  SEND pathfail to OWNER=$owner")
		}*/
	}
	
	/*
lateinit var robot   : BasicStepRobotActor 

	
	
	
	fun actionAtAnswer(MoveAnsw:String){
		println("pathexecutil | actionAtAnswer  MoveAnsw=$MoveAnsw curMove=$curMove")
 
		val answJson = JSONObject( MoveAnsw ) 
		//println("pathexecutil | actionAtAnswer $MoveAnsw answJson=$answJson")
		if( ( answJson.has("endmove") && answJson.getString("endmove") == "true")
			|| answJson.has("stepDone") ){
			pathDone = pathDone+curMove			 
			master.scope.launch{ master.autoMsg("moveok","move($curMove)") }
		}else{
			master.scope.launch{ master.autoMsg("movefail","move($curMove)") }
		}
		curMove="unknown"
	}
	
	fun register( actor: ActorBasicFsm ){
		//support =  IssWsHttpKotlinSupport.getConnectionWs(actor.scope, "${wenvAddr}:8091")
		var obsRobot  = ObserverForAnswer("obsrobot", actor.scope, ::actionAtAnswer   )
		robot   =  it.unibo.robotService.BasicStepRobotActor("stepRobot", ownerActor=obsRobot, actor.scope, "localhost")
		master = actor
	}

	fun getCurrentPath(  ) : String {
		return curPath
	}

	fun memoCurPath( msg: String  ){
		//msg(dopath,dispatch,testexec,pathexec,'dopath(path({"path":"wwlw"}),caller)',8)
		//println("pathexecutil | memoCurPath msg=$msg")
		//PROLOG version
		/*
 //2020 ApplMessage version
		val ap = it.unibo.kactor.ApplMessage(msg)
		val cc = ap.msgContent()	//a String
		println("pathexecutil | PROLOG ap=$ap cc=$cc ")
		val cct = Term.createTerm(cc)
		println("pathexecutil | PROLOG ap=$ap cc=$cc cct=$cct")
		*/
//2021 ApplMessage version
		val am      = ApplMessage.create(msg)
		val content = am.msgContent
		//println("pathexecutil | memoCurPath content=$content")	//{"path":"wwlw", "owner":"textexec"}
		val amJson = JSONObject( content )
		curPath    = amJson.get("path").toString().replace("w","p")
		//owner      = amJson.get("owner").toString()
		pathDone   = ""
		println("pathexecutil | memoCurPath curPath=$curPath ")
	}

	
	
	fun createSonarObserver(scope: CoroutineScope ){
		val support = IssWsHttpKotlinSupport.createForWs(scope, "localhost:8091" )
		//val obs     = SonarObserver("sonarobs", scope )
		//support.registerActor( obs )
	}
 */
	fun waitUser(prompt: String) {
		print(">>>  $prompt >>>  ")
		val scanner = Scanner(System.`in`)
		try {
			scanner.nextInt()
		} catch (e: java.lang.Exception) {
			e.printStackTrace()
		}
	}
}