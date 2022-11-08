/*
wsminimal.js
*/

var socket;

function connect() {
    var host = document.location.host;
    var pathname = "/"
    var addr = "ws://" + host + pathname + "socket";
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
        msg = event.data;

        console.log("ws-status:" + msg);

        let container = JSON.parse(msg);
        let pCurr = container['boxCurrentWeight']['PLASTIC'];
        let gCurr = container['boxCurrentWeight']['GLASS'];

        let trolley = container['currState'];
        let position = container['currPosition'];

        let pMax = container['boxMaxWeight']['PLASTIC'];
        let gMax = container['boxMaxWeight']['GLASS'];

        let led = container['currLedState'];

        setMessageToWindow(glassprog, gCurr.toString());
        setMessageToWindow(glassMax, gMax.toString());


        setMessageToWindow(plasticprog, pCurr.toString());
        setMessageToWindow(plasticMax, pMax.toString());

        setMessageToWindow(trolleystate, trolley.toString());
        setMessageToWindow(trolleyposition, position.toString());

        setMessageToWindow(ledstate, led.toString());

        let div = document.getElementById("table");

        if (position.toString() == "HOME"){
            div.innerHTML = ""
            generateTableHome();
        }

        if (position.toString() == "INDOOR"){
            div.innerHTML = ""
            generateTableIndoor();
        }

        if (position.toString() == "PLASTICBOX"){
            div.innerHTML = ""
            generateTablePlastic();
        }
        if(position.toString() == "GLASSBOX"){
            div.innerHTML = ""
            generateTableGlass();
        }
    };

}//connect

