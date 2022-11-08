/*
wsminimal.js
*/

let socket;

function connect() {

    let host = document.location.host;
    let pathname = "/"
    let addr = "ws://" + host + pathname + "socket";
    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

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

        let led1 = document.getElementById("led1")
        let led2 = document.getElementById("led2")
        let circle = document.getElementById("circle")
        let circle2 = document.getElementById("circle2")
        let flag = 1

        if(position.toString() == "HOME" && trolley.toString() == "IDLE"){
            led2.classList.add("d-none")
            led1.classList.remove("d-none")
            circle.style.background = "grey"
        }

        if(trolley.toString() == "STOPPED" || trolley.toString() == "PICKINGUP" || trolley.toString() == "DROPPINGOUT"){
            led2.classList.add("d-none")
            led1.classList.remove("d-none")
            circle.style.background = "green"
        }

        if(trolley.toString() == "MOVING" && position.toString() == "ONTHEROAD"){
            led1.classList.add("d-none")
            led2.classList.remove("d-none")

            setInterval(function () {
                if((flag %2) == 0){
                    circle2.style.background = "green"
                }
                else{
                    circle2.style.background = "grey"
                }
                flag++
            }, 300)         //300 ms
        }
    };

}//connect

