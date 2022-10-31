/*
wsminimal.js
*/

var socket;

function connect() {
    var host = document.location.host;
    var pathname = "/"                   //document.location.pathname;
    var addr = "ws://" + host + pathname + "socket";
    //alert("connect addr=" + addr   );

    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);


    function sendMessage(message) {
        var jsonMsg = JSON.stringify({ 'name': message });
        socket.send(jsonMsg);
        console.log("Sent Message: " + jsonMsg);
    }

    socket.onmessage = function (event) {
        //alert(`Got Message: ${event.data}`);
        msg = event.data;

        //alert(`Got Message: ${msg}`);
        console.log("ws-status:" + msg);

        let container = JSON.parse(msg);
        let pCurr = container['boxCurrentWeight']['PLASTIC'];
        let gCurr = container['boxCurrentWeight']['GLASS'];

        let pMax = container['boxMaxWeight']['PLASTIC'];
        let gMax = container['boxMaxWeight']['GLASS'];


        setMessageToWindow(glassprog, gCurr.toString());
        setMessageToWindow(glassMax, gMax.toString());


        setMessageToWindow(plasticprog, pCurr.toString());
        setMessageToWindow(plasticMax, pMax.toString());

    };


}//connect

