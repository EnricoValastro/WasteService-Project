/*
==========================================================================
SceneOnlyServer.js
sockets       = {}     //interaction with WebGLScene
postResult    != null  //a POST-request pending
==========================================================================
*/

const app          = require('express')()
const express      = require('express')
const hhtpSrv      = require('http').Server(app)
const sceneSocket  = require('socket.io')(hhtpSrv)     //interaction with WebGLScene


const sockets       = {}    //interaction with WebGLScene
let socketIndex     = -1

const serverPort    = 8090
var actorObserverClient

//STATE variables used by cmd handling on wssockets (see initWs)
var alreadyConnected = false
let runningMovesIndex   = -1
const moveMap   = new Map();    //see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Map/set
var postResult  = null

app.use(express.static('./../../WebGLScene'))

/*
-----------------------------------------------------------------------------
Defines how to handle GET from browser and from external controls
-----------------------------------------------------------------------------
*/
    app.get('/', (req, res) => {
	    console.log("SceneOnlyServer | GET socketIndex="+socketIndex + " alreadyConnected =" + alreadyConnected )
        if( ! alreadyConnected ){
            alreadyConnected = true;
            res.sendFile('indexOk.html', { root: './../../WebGLScene' })
	     }else{
		    res.sendFile('indexNoControl.html', { root: './../../WebGLScene' })
	     }
    }); //app.get

/*
-----------------------------------------------------------------------------
Defines how to handle POST from browser and from external controls
-----------------------------------------------------------------------------
*/	//USING POST : by AN Jan 2021
    app.post("/api/move", function(req, res,next)  {
	    var data = ""
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
            req.on('end', function () {	//elaborate data received
			//{ robotmove: move, time:duration } - robotmove: turnLeft | turnRight | ...
			console.log('POSTTT /api/move data ' + data + " "  + moveMap[runningMovesIndex] );
			var jsonData = JSON.parse(data)
     		var moveTodo = jsonData.robotmove
     		var duration = jsonData.time
     		if( moveTodo=="alarm"){ //do the halt move
 	            execMoveOnAllConnectedScenes(moveTodo, duration) //JULY2021 April 2002
                if( res != null ){
                    res.writeHead(200, { 'Content-Type': 'text/json' });
                    res.statusCode=200
                    var answer       = { 'endmove' : 'true' , 'move' : 'halt' }  //JSON obj
                    const answerJson = JSON.stringify(answer)
                    res.write( answerJson  );
                    res.end();
                }
                return
            }//the move is not alarm
			//console.log(" runningMovesIndex=" + runningMovesIndex + " " + moveMap[runningMovesIndex] );
			if( moveMap[runningMovesIndex] != undefined && moveMap[runningMovesIndex] != "interrupted" ){
			//the move DOES NOT 'interrupt' a move activated in asynch way
	            const answer  = { 'endmove' : "notallowed" , 'move' : moveTodo }
	            updateCallers( JSON.stringify(answer) )
                if( res != null ){
                    res.writeHead(200, { 'Content-Type': 'text/json' });
                    res.statusCode=200
                    const answerJson = JSON.stringify(answer)
                    res.write( answerJson  );
                    res.end();
                }
			} else{
	            console.log('$$$ SceneOnlyServer doMove | ' + moveTodo + " duration=" + duration  );
                postResult = res  //MEMO that a POST is running. See
	            execMoveOnAllConnectedScenes(moveTodo, duration)
			}
  	   });
	}); //app.post


//Updates the mirrors
function execMoveOnAllConnectedScenes(moveTodo, moveTime){
    if( moveTodo != "alarm") {
        runningMovesIndex++
        console.log('$$$ SceneOnlyServer | execMoveOnAllConnectedScenes '  + moveTodo + " index=" + runningMovesIndex);
        moveMap.set(runningMovesIndex, moveTodo)
        showMoveMap()
    }
	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime, runningMovesIndex) );
}

//Updates the controls and the observers (April 2022)
function updateCallers(msgJson){
    console.log("SceneOnlyServer | updateCallersssssssssssssssssss" + msgJson + " " + actorObserverClient);
    if( actorObserverClient != undefined ){
        msg = "msg(sceneEv,dispatch,scene,sceneObserver,'"+ msgJson +"',0)\n"
        actorObserverClient.write( msg );
    }
}

/*
-------------------------------------------------------------------------------------
Interact with the MASTER (the mirrors do not send any info)
-------------------------------------------------------------------------------------
*/
function sceneSocketInfoHandler() {
	console.log("SceneOnlyServer sceneSocketInfoHandler |  socketIndex="+socketIndex)
    sceneSocket.on('connection', socket => {
        socketIndex++
        console.log("SceneOnlyServer sceneSocketInfoHandler  | connection socketIndex="+socketIndex)
        const key    = socketIndex
        sockets[key] = socket
        if( socketIndex == 0) console.log("SceneOnlyServer sceneSocketInfoHandler | MASTER-webpage ready")
		socket.on( 'sonarActivated', (obj) => {  //Obj is a JSON object
			console.log( "&&& SceneOnlyServer sceneSocketInfoHandler | sonarActivated " );
			console.log(obj) 
			updateCallers( JSON.stringify(obj) )
		})

        socket.on( 'collision',     (obj) => {
		    var move =   moveMap.get(runningMovesIndex)
		    console.log( "SceneOnlyServer sceneSocketInfoHandler  | collision detected " + obj
		            + " runningMovesIndex=" + runningMovesIndex
		            + " move="+ move
		            + " target=" + obj + " numOfSockets=" + Object.keys(sockets).length );
		    showMoveMap();
		    const info     = {  'collision' : move, 'target': obj}
		    //moveMap.delete( runningMovesIndex )
		    moveMap.set(runningMovesIndex,"interrupted")
		    showMoveMap();
		    updateCallers( JSON.stringify(info) )
 		    answerToPost( JSON.stringify(info) );
 		} )
        socket.on('endmove', (moveIndex)  => {  //April2022
		    //console.log( "SceneOnlyServer sceneSocketInfoHandler  | endmove  PRE index=" + moveIndex + " moveMap.size=" + moveMap.size);
      		if( moveIndex == -1 ){
 		        console.log( "SceneOnlyServer sceneSocketInfoHandler  | endmove after collision moveMap.size=" + moveMap.size);
 		        showMoveMap();
     		    return;
      		}
      		var curMove  = moveMap.get(moveIndex)   //nome della mossa o interrupted
      		var answer   = ""
  		    console.log( "SceneOnlyServer sceneSocketInfoHandler  | endmove  curMove=" + curMove);
  		    if( curMove == "interrupted" || curMove == undefined ) { //undefined viene da SocketIO.stopMoving per alarm
  		        //answer = { 'endmove' : false , 'move' : curMove }
  		        moveMap.delete(moveIndex)
  		    }else{
                answer      = { 'endmove' : true , 'move' : curMove }
                const answerJson = JSON.stringify(answer)
                updateCallers( answerJson )
                answerToPost( answerJson );
                moveMap.delete(moveIndex)
            }
            console.log( "SceneOnlyServer sceneSocketInfoHandler  | endmove index=" + moveIndex + " moveMap.size=" + moveMap.size  );
            showMoveMap()
        })

       socket.on( 'disconnect',     () => {
        		delete sockets[key];
          		socketIndex--;
			    alreadyConnected = ( socketIndex == 0 )
        		console.log("SceneOnlyServer sceneSocketInfoHandler  | disconnect socketIndex="+socketIndex)
        })
    })
}//sceneSocketInfoHandler


function answerToPost(  answerJson ){
   if( postResult != null ){
        console.log('SceneOnlyServer | answerToPost  answer= ' + answerJson  );
        postResult.writeHead(200, { 'Content-Type': 'text/json' });
        postResult.statusCode=200
        postResult.write( answerJson  );
        postResult.end();
        postResult = null;
   }
}


function showMoveMap(){
    //moveMap.forEach( (key,v ) => console.log("WARNING: movemap key=" + key + " v="+ v) )
    moveMap.forEach( logMapElements  )
}
function logMapElements(value, key, map) {
  console.log(`MOVEMAP: moveMap[${key}] = ${value}`);
}

function setUpActorLocalConnection(actorport){
const Net = require('net');
// The port number and hostname of the server.
const host = 'localhost';
const port = actorport;

// Create a new TCP client.
actorObserverClient = new Net.Socket();
// Send a connection request to the server.

console.log("setUpActorLocalConnection TCP to:" +port + " " + actorObserverClient);

actorObserverClient.connect( { port: 8030, host: "localhost" }, function() {
     console.log('TCP connection establishedddddddddddddddddddddddd with the server.');
    // The client can now send data to the server by writing to its socket.
    //actorObserverClient.write("Hello, server.\n");
    //actorObserverClient.flush();
    updateCallers('{"scene","started"}')
});

// The client can also receive data from the server by reading from its socket.
actorObserverClient.on('data', function(chunk) {
    console.log(`Data received from the server: ${chunk.toString()}.`);
    // Request an end to the connection after the data has been received.
    client.end();
});

actorObserverClient.on('end', function() {
    console.log('Requested an end to the TCP connection');
});

}

function startServer() {
    console.log("SceneOnlyServer  | startServer" )
    //Connect with an observer actor
    setUpActorLocalConnection(8030)

    sceneSocketInfoHandler()
    hhtpSrv.listen(serverPort)
}
//MAIN
startServer()

