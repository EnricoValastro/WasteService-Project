/*
wsminimal.js
*/

    var socket;

    function sendMessage(message) {
        var jsonMsg = JSON.stringify( {'name': message});
        socket.send(jsonMsg);
        console.log("Sent Message: " + jsonMsg);
    }

    function connect(){
        var host     =  document.location.host;
        var pathname =  "/"                   //document.location.pathname;
        var addr     = "ws://" +host  + pathname + "socket"  ;
        //alert("connect addr=" + addr   );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr);

      /*  socket.onopen = function (event) {
            //console.log("Connected to " + addr);
            setMessageToWindow(infoDisplay,"Connected to " + addr);
        };*/

        socket.onmessage = function (event) {
            //alert(`Got Message: ${event.data}`);
            msg = event.data;
            //alert(`Got Message: ${msg}`);
            console.log("ws-status:" + msg);

            let container = JSON.parse(msg);
            let plasticprog = container['boxCurrentWeight']['PLASTIC'];
            let glassprog = container['boxCurrentWeight']['GLASS'];

            let plasticmax = container['boxMaxWeight']['PLASTIC'];
            let glassmax = container['boxMaxWeight']['GLASS'];

            setMessageToWindow(glassProg,glassprog.toString());
            setMessageToWindow(plasticProg, plasticprog.toString());
            setMessageToWindow(glassMax, glassmax.toString());
            setMessageToWindow(plasticMax, plasticmax.toString());
         };
    }//connect

