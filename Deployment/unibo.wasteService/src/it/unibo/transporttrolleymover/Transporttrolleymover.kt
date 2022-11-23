/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolleymover

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolleymover ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				
				lateinit var destination  : String
				var xDestination : Int = 0
				var yDestination : Int = 0
				var dir : String = "" 
				var PATH = ""
				var PATHSTILLTODO = ""
				var attempt : Int = 0
				var someToFix : Boolean = false
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.BLUE) 
						 unibo.kotlin.planner22Util.initAI()  
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
						someToFix = false 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t018",targetState="destinationEval",cond=whenRequest("moveto"))
					transition(edgeName="t019",targetState="end",cond=whenDispatch("exit"))
				}	 
				state("destinationEval") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("moveto(POS)"), Term.createTerm("moveto(POS)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													destination = payloadArg(0).trim().uppercase()
												
													xDestination = utility.ServiceAreaDestinationConfig.getXDestination(destination)
													yDestination = utility.ServiceAreaDestinationConfig.getYDestination(destination)
													dir 		 = utility.ServiceAreaDestinationConfig.getPlannerDirection(destination)
												}catch(e : Exception){}	
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="plan", cond=doswitch() )
				}	 
				state("plan") { //this:State
					action { //it:State
						
									unibo.kotlin.planner22Util.setGoal(xDestination, yDestination)
									unibo.kotlin.planner22Util.doPlan()
									PATH = unibo.kotlin.planner22Util.get_actionSequenceAsString()
									unibo.comm22.utils.ColorsOut.outappl("$name	|	moving to $destination", unibo.comm22.utils.ColorsOut.BLUE)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="execMove", cond=doswitch() )
				}	 
				state("execMove") { //this:State
					action { //it:State
						request("dopath", "dopath($PATH)" ,"pathexec" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t020",targetState="lookForFix",cond=whenReply("dopathdone"))
					transition(edgeName="t021",targetState="moveKo",cond=whenReply("dopathfail"))
				}	 
				state("lookForFix") { //this:State
					action { //it:State
						 unibo.kotlin.planner22Util.updateAfterPath(PATH)  
						 someToFix = false  
						if(  unibo.kotlin.planner22Util.getDirection() != dir  
						 ){ someToFix = true  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="fixDir", cond=doswitchGuarded({ someToFix  
					}) )
					transition( edgeName="goto",targetState="moveOk", cond=doswitchGuarded({! ( someToFix  
					) }) )
				}	 
				state("fixDir") { //this:State
					action { //it:State
						 
									PATH = utility.DirectionFixer.getPathForFixDir(unibo.kotlin.planner22Util.getDirection(), dir) 
								 	unibo.comm22.utils.ColorsOut.outappl("$name	|	fixing direction", unibo.comm22.utils.ColorsOut.BLUE)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="execMove", cond=doswitch() )
				}	 
				state("moveOk") { //this:State
					action { //it:State
						 attempt = 0  
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	arrived in $destination", unibo.comm22.utils.ColorsOut.BLUE) 
						answer("moveto", "moveok", "moveok"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("moveKo") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	moveKo", unibo.comm22.utils.ColorsOut.BLUE) 
						attempt++ 
						if( checkMsgContent( Term.createTerm("dopathfail(ARG)"), Term.createTerm("dopathfail(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													PATHSTILLTODO = payloadArg(0)
												}catch(e : Exception){}	
						}
						if( attempt==3 
						 ){ attempt = 0  
						answer("moveto", "moveko", "moveko(_)"   )  
						}
						else
						 {request("dopath", "dopath($PATHSTILLTODO)" ,"pathexec" )  
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t022",targetState="lookForFix",cond=whenReply("dopathdone"))
					transition(edgeName="t023",targetState="moveKo",cond=whenReply("dopathfail"))
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
