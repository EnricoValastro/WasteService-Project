/*
ioutils.js
*/

    const plasticProg     = document.getElementById("plasticprog");
    const plasticMax    = document.getElementById("plasticprogMax");
    const glassProg = document.getElementById("glassprog");
    const glassMax = document.getElementById("glassprogMax")

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
    }

    function addMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
          outfield.innerHTML += `<div>${output}</div>`
    }
