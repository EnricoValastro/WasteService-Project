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
					 transition(edgeName="t019",targetState="execok",cond=whenRequest("execaction"))
				}	 
				state("execok") { //this:State
					action { //it:State
						answer("execaction", "execok", "execok(_)"   )  
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
