/* Generated by AN DISI Unibo */ 
package it.unibo.ctxpi
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "raspberrypi.local", this, "wasteservice.pl", "sysRules.pl","ctxpi"
	)
}

