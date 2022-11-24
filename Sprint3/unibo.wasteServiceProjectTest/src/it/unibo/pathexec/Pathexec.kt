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
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("MockPathExec	|	starting...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						println("MockPathExec	|	waiting...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t074",targetState="work",cond=whenRequest("dopath"))
				}	 
				state("work") { //this:State
					action { //it:State
						println("MockPathExec	|	doing path in 2 second")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		//sysaction { //it:State
				 	 		  stateTimer = TimerActor("timer_work", 
				 	 			scope, context!!, "local_tout_pathexec_work", 3000.toLong() )
				 	 		//}
					}	 	 
					 transition(edgeName="t075",targetState="pathok",cond=whenTimeout("local_tout_pathexec_work"))   
					transition(edgeName="t076",targetState="pathstop",cond=whenEvent("alarm"))
				}	 
				state("pathok") { //this:State
					action { //it:State
						println("MockPathExec	|	path done")
						answer("dopath", "dopathdone", "dopathdone(ok)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("pathstop") { //this:State
					action { //it:State
						println("MockPathExec	|	path stopped")
						answer("dopath", "dopathstopped", "dopathstopped(stopped)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}