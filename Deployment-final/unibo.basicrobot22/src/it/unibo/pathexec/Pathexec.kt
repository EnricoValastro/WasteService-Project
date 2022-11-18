/* Generated by AN DISI Unibo */ 
package it.unibo.pathexec

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathexec ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var CurMoveTodo = ""    //Upcase, since var to be used in guards
		   var StepTime    = "300"
		   var PathTodo    = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						  CurMoveTodo = "" 
									StepTime = unibo.robot.robotSupport.readStepTime() //stepTimeConfig.json
						updateResourceRep( "pathexecsteptime($StepTime)"  
						)
						println("pathexec ready. StepTime=$StepTime")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="doThePath",cond=whenRequest("dopath"))
				}	 
				state("doThePath") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("dopath(PATH)"), Term.createTerm("dopath(PATH)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 PathTodo = payloadArg(0)  
								updateResourceRep( "pathexecdopath($PathTodo)"  
								)
								pathut.setPath( PathTodo  )
						}
						println("pathexec pathTodo = ${pathut.getPathTodo()}")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="nextMove", cond=doswitch() )
				}	 
				state("nextMove") { //this:State
					action { //it:State
						 CurMoveTodo = pathut.nextMove()  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="endWorkOk", cond=doswitchGuarded({ CurMoveTodo.length == 0  
					}) )
					transition( edgeName="goto",targetState="doMove", cond=doswitchGuarded({! ( CurMoveTodo.length == 0  
					) }) )
				}	 
				state("doMove") { //this:State
					action { //it:State
						delay(300) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="doMoveW", cond=doswitchGuarded({ CurMoveTodo == "w"  
					}) )
					transition( edgeName="goto",targetState="doMoveTurn", cond=doswitchGuarded({! ( CurMoveTodo == "w"  
					) }) )
				}	 
				state("doMoveTurn") { //this:State
					action { //it:State
						updateResourceRep( "pathexecdoturn($CurMoveTodo)"  
						)
						forward("cmd", "cmd($CurMoveTodo)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_doMoveTurn", 
				 	 			scope, context!!, "local_tout_pathexec_doMoveTurn", 300.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t010",targetState="nextMove",cond=whenTimeout("local_tout_pathexec_doMoveTurn"))   
					transition(edgeName="t011",targetState="stop",cond=whenEvent("alarm"))
				}	 
				state("doMoveW") { //this:State
					action { //it:State
						updateResourceRep( "pathexecdostep($CurMoveTodo)"  
						)
						request("step", "step($StepTime)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="stop",cond=whenEvent("alarm"))
					transition(edgeName="t013",targetState="nextMove",cond=whenReply("stepdone"))
					transition(edgeName="t014",targetState="endWorkKo",cond=whenReply("stepfail"))
				}	 
				state("endWorkOk") { //this:State
					action { //it:State
						println("endWorkOk: PATH DONE - BYE")
						updateResourceRep( "path $PathTodo done"  
						)
						answer("dopath", "dopathdone", "dopathdone(ok)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("stop") { //this:State
					action { //it:State
						 var PathStillTodo = pathut.getPathTodo()  
						 println("Pathexec stopped	|	$PathStillTodo")  
						answer("dopath", "dopathstopped", "dopathstopped($PathStillTodo)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_stop", 
				 	 			scope, context!!, "local_tout_pathexec_stop", 1000.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t015",targetState="s0",cond=whenTimeout("local_tout_pathexec_stop"))   
					transition(edgeName="t016",targetState="s0",cond=whenReply("stepdone"))
				}	 
				state("endWorkKo") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 var PathStillTodo = pathut.getPathTodo()  
						updateResourceRep( "pathstilltodo($PathStillTodo)"  
						)
						answer("dopath", "dopathfail", "dopathfail($PathStillTodo)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
