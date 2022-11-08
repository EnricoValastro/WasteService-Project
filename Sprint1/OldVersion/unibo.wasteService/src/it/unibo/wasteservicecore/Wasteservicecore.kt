/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservicecore

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservicecore ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				lateinit var REQMATERIAL : wasteservice.state.Material 
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						
									utility.Banner.printBannerWasteService()
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	starting...", unibo.comm22.utils.ColorsOut.CYAN) 
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
					 transition(edgeName="t07",targetState="pickup",cond=whenDispatch("dojob"))
					transition(edgeName="t08",targetState="backHome",cond=whenEvent("dropoutdone"))
					transition(edgeName="t09",targetState="end",cond=whenDispatch("exit"))
				}	 
				state("pickup") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	asking for pickup", unibo.comm22.utils.ColorsOut.CYAN) 
						if( checkMsgContent( Term.createTerm("dojob(MAT)"), Term.createTerm("dojob(MAT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											try{
											REQMATERIAL = wasteservice.state.Material.valueOf(payloadArg(0).trim().uppercase())
											}catch(e : Exception){}
						}
						request("pickup", "pickup(_)" ,"transporttrolleycore" )  
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	waiting for pickup done...", unibo.comm22.utils.ColorsOut.CYAN) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="dropout",cond=whenReply("pickupdone"))
				}	 
				state("dropout") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	asking for dropout", unibo.comm22.utils.ColorsOut.CYAN) 
						forward("dropout", "dropout($REQMATERIAL)" ,"transporttrolleycore" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("backHome") { //this:State
					action { //it:State
						 unibo.comm22.utils.ColorsOut.outappl("$name	|	sending robot to home", unibo.comm22.utils.ColorsOut.CYAN) 
						forward("gotohome", "gotohome(_)" ,"transporttrolleycore" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("end") { //this:State
					action { //it:State
						forward("exit", "exit(_)" ,"wasteservicehandler" ) 
						forward("exit", "exit(_)" ,"containermanager" ) 
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