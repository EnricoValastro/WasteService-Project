/*
ioutils.js
*/

    const glassprog       = document.getElementById("glassprog");
    const plasticprog     = document.getElementById("plasticprog");
    const glassprogMax       = document.getElementById("glassprogMax");
    const plasticprogMax     = document.getElementById("plasticprogMax");

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
    }

    function addMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
          outfield.innerHTML += `<div>${output}</div>`
    }
