//Used by sceneSubjects/Sonars.js
//subscribe called by socketIO.js
function EventBus() {
    const eventCallbacksPairs = [] 
    
    function subscribe( eventType, callback ) {
    	console.log("EventBus | subscribe eventType=" + eventType )
        const eventCallbacksPair = findEventCallbacksPair(eventType)

        if(eventCallbacksPair)
            eventCallbacksPair.callbacks.push(callback)
        else
            eventCallbacksPairs.push( new EventCallbacksPair(eventType, callback) )
    }

    function post( eventType, args ) {
        const eventCallbacksPair = findEventCallbacksPair(eventType)
     	//console.log("EventBus | post eventType=" + eventType + " " + Object(eventCallbacksPair.callbacks)  )
        //console.log("EventBus | post"   )
		//console.log( args  )
        if(!eventCallbacksPair) {
            console.error(`EventBus | no subscribers for event ${eventType}`)
            return;
        }

        eventCallbacksPair.callbacks.forEach( callback => {
        	//console.log("EventBus | post callback forEach "    )
        	callback(args)
        	} )
    }

    function findEventCallbacksPair(eventType) {
        return eventCallbacksPairs.find( eventObject => eventObject.eventType === eventType )
    }

    function EventCallbacksPair( eventType, callback ) {
        this.eventType = eventType
        this.callbacks = [callback]
    }

    return {
        subscribe,
        post
    }
}

export default new EventBus()