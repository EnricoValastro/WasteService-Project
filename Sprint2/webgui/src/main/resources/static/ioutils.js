/*
ioutils.js
*/

    const plasticprog     = document.getElementById("plasticprog");
    const glassprog    = document.getElementById("glassprog");

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
    }

    function addMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
          outfield.innerHTML += `<div>${output}</div>`
    }

