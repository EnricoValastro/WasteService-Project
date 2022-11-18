/* Generated by AN DISI Unibo */ 
package it.unibo.sonarqak22varesi

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarqak22varesi ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
			   	val sonarActorName = "sonarqak22varesi"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.YELLOW) 
						utility.configureTheSonar( sonarActorName  )
						forward("sonaractivate", "info(ok)" ,"sonarqak22varesi" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="activateTheSonar",cond=whenDispatch("sonaractivate"))
					transition(edgeName="t010",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("activateTheSonar") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	waiting...", unibo.comm22.utils.ColorsOut.YELLOW) 
						forward("sonaractivate", "info(ok)" ,"sonardatasource" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t011",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t012",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("deactivateTheSonar") { //this:State
					action { //it:State
						forward("sonardeactivate", "info(ko)" ,"sonardatasource" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="end", cond=doswitch() )
				}	 
				state("handleSonarData") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("distance(V)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = payloadArg(0)  
								 unibo.comm22.utils.ColorsOut.outappl("$name	|	emitting: ${D}", unibo.comm22.utils.ColorsOut.YELLOW) 
								emit("sonardata", "distance($D)" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t013",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t014",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("end") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	BYE", unibo.comm22.utils.ColorsOut.YELLOW) 
						 System.exit(0)  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}