/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						request("pickup", "pickup(_)" ,"transporttrolley" )  
						request("droppout", "dropout(PLASTIC)" ,"transporttrolley" )  
						if( checkMsgContent( Term.createTerm("storeWaste(MATERIAL,TRUCKLOAD)"), Term.createTerm("storewaste(M,W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  (payloadArg(0).compareTo("ciao")  
								 ){answer("storeWaste", "loadaccept", "loadaccept(_)"   )  
								}
								else
								 {answer("storeWaste", "loadrejected", "loadrejected(_)"   )  
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
