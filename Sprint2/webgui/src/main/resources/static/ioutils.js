/*
ioutils.js
*/

    const infoDisplay     = document.getElementById("display");
    //const webcamip        = document.getElementById("webcamip");
    const robotDisplay    = document.getElementById("robotDisplay");
    const pathexecDisplay = document.getElementById("pathexecDisplay");

    function setMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
         outfield.innerHTML = `<tt>${output}</tt>`
    }

    function addMessageToWindow(outfield, message) {
         var output = message.replace("\n","<br/>")
          outfield.innerHTML += `<div>${output}</div>`
    }
 
//short-hand for $(document).ready(function() { ... });
$(function () {
    $( "#h" ).click(function() { callServerUsingAjax("h") });  //callServerUsingAjax is in wsminimal
    $( "#w" ).click(function() { callServerUsingAjax("w") });
    $( "#s" ).click(function() { callServerUsingAjax("s") });
    $( "#r" ).click(function() { callServerUsingAjax("r") });
    $( "#l" ).click(function() { callServerUsingAjax("l") });
    $( "#p" ).click(function() { callServerUsingAjax("p") });
    $( "#z" ).click(function() { callServerUsingAjax("z") });
 });

function callServerUsingAjax(message) {
    //alert("callServerUsingAjax "+message)
    $.ajax({
     //imposto il tipo di invio dati (GET O POST)
      type: "POST",
      //Dove  inviare i dati
      url: "robotmove",
      //Dati da inviare
      data: "move=" + message,
      dataType: "html",
      //Visualizzazione risultato o errori
      success: function(msg){  //msg ha tutta la pagina ...
        //console.log("call msg="+msg);
        setMessageToWindow(infoDisplay,message+" done")
      },
      error: function(){
        alert("Chiamata fallita, si prega di riprovare..."); 
        //sempre meglio impostare una callback in caso di fallimento
      }
     });
}

