/* Generated by AN DISI Unibo */ 
package it.unibo.ctxwasteservice
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.14.199", this, "rasp.pl", "sysRules.pl","ctxwasteservice"
	)
}

