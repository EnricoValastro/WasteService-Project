package connQak

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import it.unibo.kactor.MsgUtil
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.IApplMessage

class connQakMqtt( ) : connQakBase( ), MqttCallback{
 	lateinit var client  : MqttClient
 	val clientid         = "clientmqtt"
	val answerTopic      = mqtttopic //"unibo/qak/$clientid" // NON RICEVE perchè usa solo dispatch

	override fun messageArrived(topic: String, msg: MqttMessage) {
        //sysUtil.traceprintln("$tt ActorBasic $name |  MQTT messageArrived on "+ topic + ": "+msg.toString());
        val m = ApplMessage( msg.toString() )
        println("       %%% connQakMqtt |  ARRIVED on $topic  m=$m  " )
    }
	override fun connectionLost(cause: Throwable?) {
        println("       %%% connQakMqtt |  MQTT connectionLost $cause " )
    }
    override fun deliveryComplete(token: IMqttDeliveryToken?) {
//		println("       %%% connQakMqtt | deliveryComplete token= "+ token );
    }
	
	fun publish( msg: String, topic: String, qos: Int = 1, retain: Boolean = false) {
		val message = MqttMessage()
		message.setRetained(retain)
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(0)
		}
		message.setPayload(msg.toByteArray())
		try {
			println("mqtt publish $msg on $topic")
			client.publish(topic, message)
		} catch (e:Exception) {
			println("       %%% connQakMqtt | publish ERROR $e topic=$topic msg=$msg"  )
 		}
	}
	
	override fun createConnection(  ){
		val brokerAddr = "tcp://$mqtthostAddr:$mqttport"
		try {
  			//println("     %%% connQakMqtt | doing connect for $clientid to $brokerAddr "  );
			client = MqttClient(brokerAddr , clientid )
			val options = MqttConnectOptions()
			options.setKeepAliveInterval(480)
			options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
			client.connect(options)
			println("       %%% connQakMqtt | connect DONE $clientid to $brokerAddr " )//+ " " + client
//Prepare to acquire answer to request
			client.setCallback(this)
			client.subscribe(answerTopic)  
			
		} catch (e: Exception) {
			println("       %%% connQakMqtt | for $clientid connect ERROR for: $brokerAddr" )
 		}
	}
	
	override fun forward(  msg: IApplMessage){
 		publish(msg.toString(), mqtttopic)		
	}
	
	override fun request(  msg: IApplMessage){
 		publish(msg.toString(), mqtttopic)
		//The answer should be in ??? unibo/qak/clientmqtt		
	}
	
	override fun emit( msg: IApplMessage ){
 		publish(msg.toString(), mqtttopic )		
	}	
}