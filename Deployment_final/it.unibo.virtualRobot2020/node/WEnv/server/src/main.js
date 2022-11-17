/*
Server
*/

const { WebpageServer, isWebpageRead } = require('./WebpageServer')
//const TCPServer                        = require('./TCPServer')

//const portNumber = 8999 //readPortNumberFromArguments()

const webpageCallbacks = {
	/*
	//NO MORE USED since the is no more the TCPServer
    onWebpageReady:   ()          => server.send( { type: 'webpage-ready ..... ',   arg: {} } ),
    onSonarActivated: object      => server.send( { type: 'sonar-activated', arg: object } ),
    onCollision:      objectName  => server.send( { type: 'collision',       arg: { objectName } } )
	*/
	/*
    onWebpageReady:   ()          => console.log( " %%% webpageCallbacks { type: 'webpage-ready ..... ',   arg: {} }" ),
    onSonarActivated: object      => { console.log( "%%% webpageCallbacks  'sonar-activated' " )
							console.log(  object ) },
    onCollision:      objectName  => console.log( "%%% webpageCallbacks { type: 'collision',  arg:  "+objectName+" " )
    */
}

const webpageServer = new WebpageServer(webpageCallbacks)
/*
const server        = new TCPServer( {
    port: portNumber,
    onClientConnected: ()      => { if(isWebpageRead()) webpageCallbacks.onWebpageReady() },
    onMessage:         command => {  
console.log("server | onMessage " + command.type + " " + command.arg + " | " + webpageServer[command.type] ); // 
//server | onMessage moveForward 800 | duration => Object.keys(sockets).forEach( key => sockets[key].emit('moveForward', duration) )
                                    webpageServer[command.type](command.arg) }
} )

function readPortNumberFromArguments() {
    const port = Number(process.argv[2])
    if(!port || port < 0 || port >= 65536) {
        console.error("This script expects a valid port number (>= 0 and < 65536) as argument.")
        process.exit()
    }

    return port
}
*/