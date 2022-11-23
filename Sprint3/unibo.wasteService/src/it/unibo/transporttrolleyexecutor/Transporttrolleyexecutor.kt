/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolleyexecutor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolleyexecutor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				lateinit var action : String	
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.BLUE) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	waiting...", unibo.comm22.utils.ColorsOut.BLUE) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t024",targetState="actionEval",cond=whenRequest("execaction"))
					transition(edgeName="t025",targetState="end",cond=whenDispatch("exit"))
				}	 
				state("actionEval") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("execaction(ACT)"), Term.createTerm("execaction(ACT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													action = payloadArg(0).trim().uppercase()
												}catch(e : Exception){}	
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="execPickup", cond=doswitchGuarded({action.equals("PICKUP") 
					}) )
					transition( edgeName="goto",targetState="execDropout", cond=doswitchGuarded({! (action.equals("PICKUP") 
					) }) )
				}	 
				state("execPickup") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	$action execution", unibo.comm22.utils.ColorsOut.BLUE) 
						delay(kotlin.random.Random.nextLong(3000, 5000)) 
						answer("execaction", "execok", "execok(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("execDropout") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	$action execution", unibo.comm22.utils.ColorsOut.BLUE) 
						delay(kotlin.random.Random.nextLong(2000, 4000)) 
						answer("execaction", "execok", "execok(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("end") { //this:State
					action { //it:State
						terminate(0)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
