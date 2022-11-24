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
			   	var advancedTestingMode = false
			   	var VALUE = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.YELLOW) 
						utility.configureTheSonar( sonarActorName  )
						 advancedTestingMode = utility.AdvancedTestingModeSetUp.checkForTestingMode()  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="advancedTestingMode", cond=doswitchGuarded({ advancedTestingMode  
					}) )
					transition( edgeName="goto",targetState="sonarActivate", cond=doswitchGuarded({! ( advancedTestingMode  
					) }) )
				}	 
				state("sonarActivate") { //this:State
					action { //it:State
						forward("sonaractivate", "info(ok)" ,"sonarqak22varesi" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t067",targetState="activateTheSonar",cond=whenDispatch("sonaractivate"))
					transition(edgeName="t068",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("advancedTestingMode") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	ADVANCED TESTING MODE: ON", unibo.comm22.utils.ColorsOut.YELLOW) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitForRequest", cond=doswitch() )
				}	 
				state("waitForRequest") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t069",targetState="produceData",cond=whenRequest("produce"))
				}	 
				state("produceData") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("produce(VAL)"), Term.createTerm("produce(VAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 VALUE = payloadArg(0).toInt()  
						}
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	ADVANCED TESTING MODE: producing $VALUE", unibo.comm22.utils.ColorsOut.YELLOW) 
						emit("sonardata", "distance($VALUE)" ) 
						answer("produce", "produceok", "produceok(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitForRequest", cond=doswitch() )
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
					 transition(edgeName="t070",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t071",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
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
					 transition(edgeName="t072",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t073",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
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