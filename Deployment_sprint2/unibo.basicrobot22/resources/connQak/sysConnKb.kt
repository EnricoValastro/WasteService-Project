package connQak

import connQak.ConnectionType

val mqtthostAddr    = "broker.hivemq.com"	//broker.hivemq.com
val mqttport		= "1883" 
val mqtttopic       = "unibo/basicrobot"
val hostAddr 		= "localhost" //   172.17.0.2 "192.168.1.5" "localhost"
val port     		= "8020"
val qakdestination 	= "basicrobot"
val ctxqakdest      = "ctxbasicrobot"
val connprotocol    = ConnectionType.TCP //TCP COAP HTTP MQTT  ConnectionType.TCP

//fun main(){ println("consoles") }