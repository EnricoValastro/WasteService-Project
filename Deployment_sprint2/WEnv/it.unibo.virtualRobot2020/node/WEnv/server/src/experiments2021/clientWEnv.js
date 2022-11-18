#!/usr/bin/env node
var WebSocketClient = require('websocket').client;

var client = new WebSocketClient();

client.on('connectFailed', function(error) {
    console.log('Connect Error: ' + error.toString());
});

client.on('connect', function(connection) {
    console.log('WebSocket Client Connected');

    connection.on('error', function(error) {
        console.log("Connection Error: " + error.toString());
    });
    connection.on('close', function() {
        console.log('echo-protocol Connection Closed');
    });
    connection.on('message', function(message) {
        if (message.type === 'utf8') {
            console.log("Received: '" + message.utf8Data + "'");
        }
    });

    function sendCmd() {
        if (connection.connected) {
            //var number = Math.round(Math.random() * 0xFFFFFF);
            var move = '{"robotmove":"moveForward", "time":800}'
            connection.sendUTF( move );
            //setTimeout(sendCmd, 1000);
        }
    }
    sendCmd();
});

client.connect("ws://localhost:8091/");
/*Getting a 200 status response, means that the request didn't reach your WebSocket Handler.*/