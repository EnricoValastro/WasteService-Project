/* Generated by AN DISI Unibo */ 
package it.unibo.wasteserviceactor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteserviceactor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "setup"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val boxMaxWeight = mutableMapOf<wasteservice.state.Material, Double>(wasteservice.state.Material.PLASTIC to 500.0, wasteservice.state.Material.GLASS to 500.0)
				val boxState  = wasteservice.state.WasteServiceState(boxMaxWeight)
				lateinit var requestMaterialToStore : wasteservice.state.Material 
				var requestWeightToStore = 0.0
		return { //this:ActionBasciFsm
				state("setup") { //this:State
					action { //it:State
						println("$name | setup")
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						println("$name	|	in idle ")
						updateResourceRep(boxState.toJsonString() 
						)
					}
					 transition(edgeName="t00",targetState="requestEvaluation",cond=whenRequest("storeWaste"))
				}	 
				state("requestEvaluation") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storeWaste(MATERIAL,TRUCKLOAD)"), Term.createTerm("storeWaste(MAT,QUA)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												try{
													requestMaterialToStore = wasteservice.state.Material.valueOf(payloadArg(0).trim().uppercase())  
													requestWeightToStore = payloadArg(1).toDouble()	
													
								if(  boxState.canStore(requestMaterialToStore, requestWeightToStore)  
								 ){ boxState.updateBoxWeight(requestMaterialToStore, requestWeightToStore)  
								answer("storeWaste", "loadaccept", "loadaccept(_)"   )  
								}
								else
								 {answer("storeWaste", "loadrejected", "loadrejected(_)"   )  
								 }
								
													
												}catch(e : Exception){
								answer("storeWaste", "loadrejected", "loadrejected(_)"   )  
								
												}	
						}
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}