/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolleycore

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolleycore ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				lateinit var MaterialToStore : String
				lateinit var POS 			 : String
				val tTState = transporttrolley.state.TransportTrolleyState()
				var updateFlag = 0
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						utility.Banner.transportTrolleyBanner() 
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.CYAN) 
						updateResourceRep(tTState.toJsonString() 
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
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	waiting...", unibo.comm22.utils.ColorsOut.CYAN) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="pickupMove",cond=whenRequest("pickup"))
					transition(edgeName="t01",targetState="dropoutMove",cond=whenDispatch("dropout"))
					transition(edgeName="t02",targetState="backHome",cond=whenDispatch("gotohome"))
					transition(edgeName="t03",targetState="end",cond=whenDispatch("exit"))
				}	 
				state("pickupMove") { //this:State
					action { //it:State
						request("moveto", "moveto(INDOOR)" ,"transporttrolleymover" )  
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.MOVING)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="pickupExec",cond=whenReply("moveok"))
					transition(edgeName="t05",targetState="moveErr",cond=whenReply("moveko"))
				}	 
				state("pickupExec") { //this:State
					action { //it:State
						request("execaction", "execaction(PICKUP)" ,"transporttrolleyexecutor" )  
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.PICKINGUP)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.INDOOR)
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="pickupRes",cond=whenReply("execok"))
					transition(edgeName="t07",targetState="execErr",cond=whenReply("execko"))
				}	 
				state("pickupRes") { //this:State
					action { //it:State
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.IDLE)
						updateResourceRep(tTState.toJsonString() 
						)
						answer("pickup", "pickupdone", "pickupdone(_)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("dropoutMove") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("dropout(MATERIAL)"), Term.createTerm("dropout(MATERIAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									
												try{
													MaterialToStore = payloadArg(0).trim().uppercase()
													if(MaterialToStore.equals("PLASTIC")){
														POS = "PLASTICBOX"
													}
													else{
														POS = "GLASSBOX"
													}
												}catch(e : Exception){}	
						}
						request("moveto", "moveto($POS)" ,"transporttrolleymover" )  
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.MOVING)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="dropoutExec",cond=whenReply("moveok"))
					transition(edgeName="t09",targetState="moveErr",cond=whenReply("moveko"))
				}	 
				state("dropoutExec") { //this:State
					action { //it:State
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.DROPPINGOUT)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.valueOf(POS))
						updateResourceRep(tTState.toJsonString() 
						)
						request("execaction", "execaction(DROPOUT)" ,"transporttrolleyexecutor" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="dropoutRes",cond=whenReply("execok"))
					transition(edgeName="t011",targetState="execErr",cond=whenReply("execko"))
				}	 
				state("dropoutRes") { //this:State
					action { //it:State
						emit("dropoutdone", "dropoutdone" ) 
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.IDLE)
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("backHome") { //this:State
					action { //it:State
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.MOVING)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.ONTHEROAD)
						updateResourceRep(tTState.toJsonString() 
						)
						request("moveto", "moveto(HOME)" ,"transporttrolleymover" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="backHomeRes",cond=whenReply("moveok"))
					transition(edgeName="t013",targetState="moveErr",cond=whenReply("moveko"))
				}	 
				state("backHomeRes") { //this:State
					action { //it:State
						
									tTState.setCurrState(transporttrolley.state.CurrStateTrolley.IDLE)
									tTState.setCurrPosition(transporttrolley.state.TTPosition.HOME)	
						updateResourceRep(tTState.toJsonString() 
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("moveErr") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	something went wrong...assistance required.", unibo.comm22.utils.ColorsOut.CYAN) 
						forward("exit", "exit(_)" ,"wasteservicecore" ) 
						forward("exit", "exit(_)" ,"transporttrolleycore" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("execErr") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("end") { //this:State
					action { //it:State
						forward("exit", "exit(_)" ,"transporttrolleyexecutor" ) 
						forward("exit", "exit(_)" ,"transporttrolleymover" ) 
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
