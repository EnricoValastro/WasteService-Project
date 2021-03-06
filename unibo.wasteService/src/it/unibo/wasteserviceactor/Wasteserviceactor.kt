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
		return "idle"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var requestMaterialToStore = ""
				var requestWeightToStore = 0.0
				val currentWeightsMap = mutableMapOf<String, Double>("Plastic" to 0.0, "Glass" to 0.0)
				val maxWeightsMap = mutableMapOf<String, Double>("Plastic" to 500.0, "Glass" to 500.0)
		return { //this:ActionBasciFsm
				state("idle") { //this:State
					action { //it:State
						println("$name	|	in idle ")
					}
					 transition(edgeName="t00",targetState="requestEvaluation",cond=whenRequest("storeWaste"))
				}	 
				state("requestEvaluation") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storeWaste(MAT,QUA)"), Term.createTerm("storeWaste(MAT,QUA)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												requestMaterialToStore = payloadArg(0)
												requestWeightToStore = payloadArg(1).toDouble()
												val currentWeight = currentWeightsMap[requestMaterialToStore]
												
								if(  currentWeight != null && 
												(currentWeight + requestWeightToStore) <= maxWeightsMap[requestMaterialToStore]!!  
								 ){ currentWeightsMap[requestMaterialToStore] = currentWeight + requestWeightToStore  
								answer("storeWaste", "loadaccept", "loadaccept(_)"   )  
								}
								else
								 {answer("storeWaste", "loadrejected", "loadrejected(_)"   )  
								 }
						}
					}
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}
