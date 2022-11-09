/* Generated by AN DISI Unibo */ 
package it.unibo.systemstatemanager

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Systemstatemanager ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				val system  = sys.state.SystemState()
				lateinit var requestMaterialToStore : sys.state.Material 
				var requestWeightToStore 			: Double = 0.0	
				lateinit var position 				: sys.state.TTPosition
				lateinit var state 					: sys.state.CurrStateTrolley
				lateinit var led 					: sys.state.CurrStateLed
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.MAGENTA) 
						updateResourceRep(system.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	waiting...", unibo.comm22.utils.ColorsOut.MAGENTA) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t026",targetState="updateContainerState",cond=whenDispatch("updatecontainer"))
					transition(edgeName="t027",targetState="updateTrolleyState",cond=whenDispatch("updatetrolley"))
					transition(edgeName="t028",targetState="updateLedState",cond=whenDispatch("updateled"))
					transition(edgeName="t029",targetState="sendData",cond=whenDispatch("getdata"))
					transition(edgeName="t030",targetState="replyData",cond=whenRequest("getledstate"))
					transition(edgeName="t031",targetState="replyData",cond=whenRequest("getcontainterstate"))
					transition(edgeName="t032",targetState="replyData",cond=whenRequest("gettrolleystate"))
					transition(edgeName="t033",targetState="replyData",cond=whenRequest("gettrolleyposition"))
					transition(edgeName="t034",targetState="end",cond=whenDispatch("exit"))
				}	 
				state("updateContainerState") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	updating container state", unibo.comm22.utils.ColorsOut.MAGENTA) 
						if( checkMsgContent( Term.createTerm("updatecontainer(MAT,QUA)"), Term.createTerm("updatecontainer(MAT,QUA)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													requestMaterialToStore = sys.state.Material.valueOf(payloadArg(0).trim().uppercase())  
													requestWeightToStore = payloadArg(1).toDouble()	
													system.updateBoxWeight(requestMaterialToStore, requestWeightToStore)
												}catch(e : Exception){
													//TobeDone
												}
						}
						updateResourceRep( system.toJsonString()  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("updateTrolleyState") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	updating trolley state", unibo.comm22.utils.ColorsOut.MAGENTA) 
						if( checkMsgContent( Term.createTerm("updatetrolley(POS,STAT)"), Term.createTerm("updatetrolley(POS,STAT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													position = sys.state.TTPosition.valueOf(payloadArg(0).trim().uppercase())  
													state = sys.state.CurrStateTrolley.valueOf(payloadArg(1).trim().uppercase())	
													system.setCurrState(state)
													system.setCurrPosition(position)
												}catch(e : Exception){
													//TobeDone
												}
						}
						updateResourceRep( system.toJsonString()  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("updateLedState") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	updating led state", unibo.comm22.utils.ColorsOut.MAGENTA) 
						if( checkMsgContent( Term.createTerm("updateled(STAT)"), Term.createTerm("updateled(STAT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													led = sys.state.CurrStateLed.valueOf(payloadArg(0).trim().uppercase())  
													
													system.setCurrLedState(led)
												}catch(e : Exception){
													//TobeDone
												}
						}
						updateResourceRep( system.toJsonString()  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("sendData") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	sending data to someone", unibo.comm22.utils.ColorsOut.MAGENTA) 
						delay(1000) 
						updateResourceRep( system.toJsonString()  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("replyData") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("getledstate(_)"), Term.createTerm("getledstate(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var DATA = system.getCurrLedState().toString()  
								answer("getledstate", "givedata", "givedata($DATA)"   )  
						}
						if( checkMsgContent( Term.createTerm("getcontainerstate(_)"), Term.createTerm("getcontainerstate(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var DATA = system.getAllCurrentBoxWeight().toString()  
								answer("getledstate", "givedata", "givedata($DATA)"   )  
						}
						if( checkMsgContent( Term.createTerm("gettrolleyposition(_)"), Term.createTerm("gettrolleyposition(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var DATA = system.getCurrPosition().toString()  
								answer("getledstate", "givedata", "givedata($DATA)"   )  
						}
						if( checkMsgContent( Term.createTerm("gettrolleystate(_)"), Term.createTerm("gettrolleystate(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var DATA = system.getCurrState().toString()  
								answer("getledstate", "givedata", "givedata($DATA)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("end") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	bye", unibo.comm22.utils.ColorsOut.MAGENTA) 
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
