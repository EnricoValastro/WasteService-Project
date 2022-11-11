package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import java.io.BufferedOutputStream
import java.io.OutputStreamWriter

object nanoSupport {
	lateinit var writer : OutputStreamWriter

	fun create( owner : ActorBasic ){		
		val p = Runtime.getRuntime().exec("sudo ./Motors")		 	
 		writer = OutputStreamWriter(  p.getOutputStream()  )
		println("motorscSupport | CREATED with writer=$writer")
 	}

	fun move( msg : String="" ){
		//println("motorscSupport | WRITE $msg with $writer")
		writer.write( msg )
		writer.flush()
	}
	
	fun terminate(){
		
	}

}