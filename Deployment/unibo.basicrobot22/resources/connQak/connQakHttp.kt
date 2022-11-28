package connQak

import java.net.URL
 
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.client.methods.HttpPost
import java.io.InputStream
import org.apache.http.impl.client.CloseableHttpClient
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.IApplMessage

//See https://www.codejava.net/java-se/networking/how-to-use-java-urlconnection-and-httpurlconnection

/*
 * The interaction is with an instance of httpserver/server.kt
 */

class connQakHttp( ) : connQakBase( ){
lateinit var client   : CloseableHttpClient  
lateinit var hostAddr : String 
	 
 		
	override fun createConnection(  ){
		hostAddr =  "http://$hostAddr:$port"    //"http://localhost:8080"
		println("connQakHttp | createConnection hostAddr=$")
		client   =  HttpClientBuilder.create().build()
 	}
	override fun forward(  msg: IApplMessage){
 		 	
	}
	
	override fun request(  msg: IApplMessage){
 	}
	
	override fun emit( msg: IApplMessage ){
  	}	
	
//	override fun forward( move : String ){
//		println("connQakHttp | forward $move")
//		val response = client.execute( HttpPost(hostAddr+"/$move")) //HttpResponse
//		showResponse( "POST $move", response.getEntity().getContent() )
//		//println("connQakHttp | response $response")
//	}
//	
//	override fun request( move : String ){
//		println("connQakHttp | request $move")
//		val post = HttpPost(hostAddr+"/$move")		     //HttpPost
//		post.setHeader("Content-Type","text/plain")		//could be "application/json"
//		post.setHeader("Accept", "text/plain")
//		val response = client.execute( post )           //HttpResponse
//		//println("connQakHttp | response ${response.getStatusLine().getStatusCode()}")
//		showResponse( "POST $move", response.getEntity().getContent() )
// 	}
//	
//	override fun emit( ev : String ){
//		println("connQakHttp | emit $ev")
//		val response = client.execute( HttpPost(hostAddr+"/$ev")) //HttpResponse
//		showResponse( "POST $ev", response.getEntity().getContent() )
// 	}
	
	fun showResponse(  msg : String, inps : InputStream ){
		inps.bufferedReader().use {
			val response = StringBuffer()
			var inputLine = it.readLine()
			while (inputLine != null) {
				response.append(inputLine)
				inputLine = it.readLine()
			}
			it.close()
			println(msg)
			println(response.toString())
		}	
	}
	
	
	
}