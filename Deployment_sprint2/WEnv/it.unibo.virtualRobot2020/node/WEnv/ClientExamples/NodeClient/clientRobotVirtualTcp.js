/*
 * it.unibo.frontend/nodeCode/frontend/jsCode/clientRobotVirtual.js
 * using TCP
 */
const net       = require('net')
const SEPARATOR = ";"
const client    = new Client({ip: "localhost", port: 8999})

function Client({ port, ip }) {
    const self = this
    let clientSocket
    const outQueue = []
    connectTo(port, ip)    
    function flushOutQueue() {
        while(outQueue.length !== 0) {
            const data = outQueue.shift()
            self.send(data)
        }
    }
    function connectTo(port, ip) {
	        const client = new net.Socket()
	        clientSocket = client
	        client.connect({ port, ip }, () => console.log(`\t clientRobotVirtual Connecting...`) )
        client.on('connect', () => {
            console.log(`\t clientRobotVirtual Connected`)
            flushOutQueue()
            //test()
        })
        client.on('data', message => {
            String(message)
                    .split(SEPARATOR)
                    .map( string => string.trim() )
                    .filter( string => string.length !== 0  )
                    .map( JSON.parse )
                    .forEach( message => console.log( message) )
        })       
        client.on('close', () =>  console.log(`\t clientRobotVirtual Connection closed`) )
        client.on('error', () => console.log(`\t clientRobotVirtual Connection error`) )
    }
    this.send = function(message) {
        if(!clientSocket.connecting)
            clientSocket.write(SEPARATOR +message +SEPARATOR)
        else {
            console.log(`\tSocket not created, message added to queue`)
            outQueue.push(message)
        }
    }
    this.finish = function() {
        if(clientSocket.connecting)
            clientSocket.on('connect', clientSocket.end )
        else
            clientSocket.end()
    }
}

function test(){
	var msg = "{\"type\": \"turnLeft\", \"arg\": 800 }";
	console.log("sending " + msg + " to " + client);
	client.send(msg);
}

test();

module.exports=client;