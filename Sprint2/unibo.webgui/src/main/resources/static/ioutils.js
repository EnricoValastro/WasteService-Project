/*
ioutils.js
*/

    const glassprog       = document.getElementById("glassCurrent");
    const plasticprog     = document.getElementById("plasticCurrent");
    const glassMax       = document.getElementById("glassMax");
    const plasticMax     = document.getElementById("plasticMax");

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
    }

    function addMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
          outfield.innerHTML += `<div>${output}</div>`
    }
