/*
axiosClientToWenv
walks along the boundary
*/
const axios = require('axios')
const URL   = 'http://localhost:8090/api/move' ;
const path = ['sud', 'east', 'north', 'west']

/*
Using callbacks, no global state
*/

function ahead(numOfSteps){
     domove("moveForward", numOfSteps, goon, ko)
}

function goon(numOfSteps){
     domove("moveForward", numOfSteps, ahead, ko)
}

function ko(numOfSteps){
console.log("...............................................ko numOfSteps=" + numOfSteps  )
    if( numOfSteps++ < 4 )  domove("turnLeft", numOfSteps, goon, ko)
    else  domove("turnLeft", numOfSteps, terminate, terminate)  //just to return to initial state
}

function terminate(){
    console.log("Buoundary explored"   )
}

ahead(1)    //boundary

//------------------------------------------------------------
function domove(move, numOfSteps, callbackOk, callbackCollision)  {
    axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
    }).then(response => {   //continues when the action has been done
        console.log("axiosClientToWenv domove | response.data: " )
        console.log( response.data )
        var collision = JSON.parse( response.data ).collision == "true"
        console.log( "domove move=" + move + " numOfSteps=" + numOfSteps + " collision= " + collision  )
        if( collision ) callbackCollision(numOfSteps); else callbackOk(numOfSteps)
  })
  .catch(error => {
    console.error(error)
  })
}
